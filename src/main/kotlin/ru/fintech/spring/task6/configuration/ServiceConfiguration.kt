package ru.fintech.spring.task6.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.fintech.spring.task6.exception.RestTemplateResponseErrorHandler
import java.time.Duration


@Configuration
class ServiceConfiguration {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder
        .errorHandler(RestTemplateResponseErrorHandler())
        .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_IN_SECONDS))
        .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_IN_SECONDS))
        .build()

//    @Bean
//    fun dataSource(): DataSource? {
//        return EmbeddedDatabaseBuilder()
//            .setType(EmbeddedDatabaseType.H2)
//            .addScript("classpath:jdbc/schema.sql").build()
//    }

    companion object {
        private const val CONNECT_TIMEOUT_IN_SECONDS = 30L
        private const val READ_TIMEOUT_IN_SECONDS = 60L
    }
}





