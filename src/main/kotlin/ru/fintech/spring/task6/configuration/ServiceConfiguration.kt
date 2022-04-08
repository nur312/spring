package ru.fintech.spring.task6.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.fintech.spring.task6.exception.RestTemplateResponseErrorHandler
import java.time.Duration


@Configuration
class ServiceConfiguration {

    @Bean
    fun restTemplate(
        builder: RestTemplateBuilder,
        @Value("\${timeoutinseconds.connect}") connectTimeout: Long,
        @Value("\${timeoutinseconds.read}") readTimeout: Long
    ): RestTemplate = builder
        .errorHandler(RestTemplateResponseErrorHandler())
        .setConnectTimeout(Duration.ofSeconds(connectTimeout))
        .setReadTimeout(Duration.ofSeconds(readTimeout))
        .build()
}