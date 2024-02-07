package org.genspectrum.lapis.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.luben.zstd.ZstdOutputStream
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.genspectrum.lapis.util.CachedBodyHttpServletRequest
import org.genspectrum.lapis.util.HeaderModifyingRequestWrapper
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders.ACCEPT_ENCODING
import org.springframework.http.HttpHeaders.CONTENT_ENCODING
import org.springframework.http.HttpHeaders.TRANSFER_ENCODING
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.OutputStream
import java.util.Enumeration
import java.util.zip.GZIPOutputStream

private val log = KotlinLogging.logger {}

enum class Compression(val value: String, val compressionOutputStreamFactory: (OutputStream) -> OutputStream) {
    GZIP("gzip", ::GZIPOutputStream),
    ZSTD("zstd", { ZstdOutputStream(it).apply { commitUnderlyingResponseToPreventContentLengthFromBeingSet() } }),
    ;

    companion object {
        fun fromHeaders(acceptEncodingHeaders: Enumeration<String>?): Compression? {
            if (acceptEncodingHeaders == null) {
                return null
            }

            val headersList = acceptEncodingHeaders.toList()

            return when {
                headersList.contains(GZIP.value) -> GZIP
                headersList.contains(ZSTD.value) -> ZSTD
                else -> null
            }
        }
    }
}

// https://github.com/apache/tomcat/blob/10e3731f344cd0d018d4be2ee767c105d2832283/java/org/apache/catalina/connector/OutputBuffer.java#L223-L229
fun ZstdOutputStream.commitUnderlyingResponseToPreventContentLengthFromBeingSet() {
    val nothing = ByteArray(0)
    write(nothing)
}

@Component
@Order(DOWNLOAD_AS_FILE_FILTER_ORDER - 1)
class CompressionFilter(val objectMapper: ObjectMapper) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val reReadableRequest = CachedBodyHttpServletRequest(request, objectMapper)

        val requestWithContentEncoding = HeaderModifyingRequestWrapper(
            reReadableRequest = reReadableRequest,
            headerName = ACCEPT_ENCODING,
            computeHeaderValueFromRequest = ::computeAcceptEncodingValueFromRequest,
        )

        val maybeCompressingResponse = createMaybeCompressingResponse(
            response,
            requestWithContentEncoding.getHeaders(ACCEPT_ENCODING),
        )

        filterChain.doFilter(
            requestWithContentEncoding,
            maybeCompressingResponse,
        )

        maybeCompressingResponse.outputStream.flush()
        maybeCompressingResponse.outputStream.close()
    }

    private fun createMaybeCompressingResponse(
        response: HttpServletResponse,
        acceptEncodingHeaders: Enumeration<String>?,
    ) = when (val compression = Compression.fromHeaders(acceptEncodingHeaders)) {
        null -> response
        else -> CompressingResponse(response, compression)
    }
}

private fun computeAcceptEncodingValueFromRequest(reReadableRequest: CachedBodyHttpServletRequest) =
    when (reReadableRequest.getStringField(COMPRESSION_PROPERTY)) {
        Compression.GZIP.value -> Compression.GZIP.value
        Compression.ZSTD.value -> Compression.ZSTD.value
        else -> null
    }

class CompressingResponse(
    response: HttpServletResponse,
    compression: Compression,
) : HttpServletResponse by response {
    init {
        log.info { "Compressing using $compression" }
        response.setHeader(CONTENT_ENCODING, compression.value)
        preventSpringFromSettingTheContentLengthWhichIsUnknownWhenCompressing(response)
    }

    private fun preventSpringFromSettingTheContentLengthWhichIsUnknownWhenCompressing(response: HttpServletResponse) {
        response.addHeader(TRANSFER_ENCODING, "chunked")
    }

    private val servletOutputStream = CompressingServletOutputStream(response.outputStream, compression)

    override fun getOutputStream() = servletOutputStream
}

class CompressingServletOutputStream(
    private val outputStream: ServletOutputStream,
    compression: Compression,
) : ServletOutputStream() {
    private val compressingStream = compression.compressionOutputStreamFactory(outputStream)

    override fun write(byte: Int) {
        compressingStream.write(byte)
    }

    override fun isReady() = outputStream.isReady

    override fun setWriteListener(listener: WriteListener?) = outputStream.setWriteListener(listener)

    override fun close() {
        super.close()
        compressingStream.close()
    }

    override fun flush() {
        super.flush()
        compressingStream.flush()
    }
}
