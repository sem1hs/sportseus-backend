package com.semihsahinoglu.sportseus.football.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "football")
data class FootballApiProperties(
    val baseUrl: String,
    val apiKey: String,
)
