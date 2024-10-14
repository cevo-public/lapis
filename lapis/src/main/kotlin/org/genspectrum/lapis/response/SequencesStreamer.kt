package org.genspectrum.lapis.response

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.genspectrum.lapis.controller.LapisHeaders.LAPIS_DATA_VERSION
import org.genspectrum.lapis.controller.LapisMediaType.TEXT_X_FASTA
import org.genspectrum.lapis.silo.DataVersion
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.util.stream.Stream

@Component
class SequencesStreamer(
    private val dataVersion: DataVersion,
    private val objectMapper: ObjectMapper,
) {
    fun stream(
        sequenceData: Stream<SequenceData>,
        response: HttpServletResponse,
        acceptHeaders: List<MediaType>,
    ) {
        response.setHeader(LAPIS_DATA_VERSION, dataVersion.dataVersion)

        for (acceptHeader in acceptHeaders) {
            if (TEXT_X_FASTA.includes(acceptHeader)) {
                streamFasta(response, sequenceData)
                return
            }

            if (MediaType.APPLICATION_JSON.includes(acceptHeader)) {
                streamJson(response, sequenceData)
                return
            }

            if (MediaType.APPLICATION_NDJSON.includes(acceptHeader)) {
                streamNdjson(response, sequenceData)
                return
            }
        }

        streamFasta(response, sequenceData)
    }

    private fun streamFasta(
        response: HttpServletResponse,
        sequenceData: Stream<SequenceData>,
    ) {
        if (response.contentType == null) {
            response.contentType = MediaType(TEXT_X_FASTA, Charset.defaultCharset()).toString()
        }

        response.outputStream.writer().use { stream ->
            sequenceData.filter { it.sequence != null }
                .forEach { stream.appendLine(it.writeAsFasta()) }
        }
    }

    private fun streamJson(
        response: HttpServletResponse,
        sequenceData: Stream<SequenceData>,
    ) {
        if (response.contentType == null) {
            response.contentType = MediaType(MediaType.APPLICATION_JSON, Charset.defaultCharset()).toString()
        }

        var isFirstEntry = true
        response.outputStream.writer().use { stream ->
            stream.append('[')
            sequenceData.filter { it.sequence != null }
                .forEach {
                    if (isFirstEntry) {
                        isFirstEntry = false
                    } else {
                        stream.append(',')
                    }
                    stream.append(objectMapper.writeValueAsString(it))
                }
            stream.append(']')
        }
    }

    private fun streamNdjson(
        response: HttpServletResponse,
        sequenceData: Stream<SequenceData>,
    ) {
        if (response.contentType == null) {
            response.contentType = MediaType(MediaType.APPLICATION_NDJSON, Charset.defaultCharset()).toString()
        }

        response.outputStream.writer().use { stream ->
            sequenceData.filter { it.sequence != null }
                .forEach { stream.appendLine(objectMapper.writeValueAsString(it)) }
        }
    }
}