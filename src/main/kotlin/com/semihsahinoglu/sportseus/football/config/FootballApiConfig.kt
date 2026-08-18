package com.semihsahinoglu.sportseus.football.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(FootballApiProperties::class)
class FootballApiConfig {

    @Bean
    fun footballRestClient(props: FootballApiProperties): RestClient =
        RestClient.builder()
            .baseUrl(props.baseUrl)
            .defaultHeader("x-apisports-key", props.apiKey)   // API-Football kimlik header'ı
            .build()
}