package ru.fintech.spring.task6.client

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import ru.fintech.spring.task6.dto.QuoteDto


@Service
class RandomQuoteClient(private val restTemplate: RestTemplate) {

    fun getQuote(): String = restTemplate.getForObject<QuoteDto>(GET_RANDOM_QUOTE_URL).quote

    companion object {
        private const val GET_RANDOM_QUOTE_URL = "https://api.kanye.rest/"
    }
}
