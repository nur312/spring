package ru.fintech.spring.task6.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import ru.fintech.spring.task6.dto.QuoteDto


@Service
class RandomQuoteClient(
    private val restTemplate: RestTemplate,
    @Value("\${url.randomquote}") private val getRandomQuoteUrl: String
) {

    fun getQuote(): String = restTemplate.getForObject<QuoteDto>(getRandomQuoteUrl).quote
}
