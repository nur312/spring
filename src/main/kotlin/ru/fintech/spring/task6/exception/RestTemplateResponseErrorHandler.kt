package ru.fintech.spring.task6.exception

import org.slf4j.LoggerFactory
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler

class RestTemplateResponseErrorHandler : ResponseErrorHandler {

    private val log = LoggerFactory.getLogger(javaClass)


    override fun hasError(httpResponse: ClientHttpResponse): Boolean =
        httpResponse.statusCode.is4xxClientError || httpResponse.statusCode.is5xxServerError


    override fun handleError(httpResponse: ClientHttpResponse) {
        if (httpResponse.statusCode.is4xxClientError) {
            log.warn("Bad request to the external server", httpResponse)
        } else if (httpResponse.statusCode.is5xxServerError) {
            log.warn("The external server error", httpResponse)
        }
    }
}