package org.genspectrum.lapis

import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.genspectrum.lapis.auth.DataOpennessAuthorizationFilterFactory
import org.genspectrum.lapis.config.DatabaseConfig
import org.genspectrum.lapis.config.LapisApplicationProperties
import org.genspectrum.lapis.config.SequenceFilterFields
import org.genspectrum.lapis.logging.RequestContext
import org.genspectrum.lapis.logging.RequestContextLogger
import org.genspectrum.lapis.logging.StatisticsLogObjectMapper
import org.genspectrum.lapis.util.TimeFactory
import org.genspectrum.lapis.util.YamlObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter
import java.io.File

@Configuration
class LapisSpringConfig {
    @Bean
    fun openAPI(sequenceFilterFields: SequenceFilterFields, databaseConfig: DatabaseConfig) =
        buildOpenApiSchema(sequenceFilterFields, databaseConfig)

    @Bean
    fun databaseConfig(
        lapisApplicationProperties: LapisApplicationProperties,
        yamlObjectMapper: YamlObjectMapper,
    ): DatabaseConfig {
        val path = lapisApplicationProperties.databaseConfig.path
            ?: throw IllegalStateException(
                "Database config path is not set, set it with '--lapis.database-config.path'",
            )
        return yamlObjectMapper.objectMapper.readValue(File(path))
    }

    @Bean
    fun sequenceFilterFields(databaseConfig: DatabaseConfig) = SequenceFilterFields.fromDatabaseConfig(databaseConfig)

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(false)
        filter.setAfterMessagePrefix("REQUEST DATA: ")
        return filter
    }

    @Bean
    fun requestContextLogger(
        requestContext: RequestContext,
        statisticsLogObjectMapper: StatisticsLogObjectMapper,
        timeFactory: TimeFactory,
    ) = RequestContextLogger(
        requestContext,
        statisticsLogObjectMapper,
        KotlinLogging.logger("StatisticsLogger"),
        timeFactory,
    )

    @Bean
    fun dataOpennessAuthorizationFilter(
        dataOpennessAuthorizationFilterFactory: DataOpennessAuthorizationFilterFactory,
    ) = dataOpennessAuthorizationFilterFactory.create()
}
