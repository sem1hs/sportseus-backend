package com.semihsahinoglu.sportseus.security.entity

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth.refresh-cookie")
data class RefreshCookieProperties(
    val name: String,
    val path: String,
    val sameSite: String,
    val secure: Boolean,
    val maxAgeDays: Long,
)