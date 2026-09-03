package com.semihsahinoglu.sportseus.auth.service

import com.semihsahinoglu.sportseus.security.entity.RefreshCookieProperties
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RefreshTokenCookieFactory(
    private val props: RefreshCookieProperties,
) {
    fun create(refreshToken: String): ResponseCookie =
        base(refreshToken)
            .maxAge(Duration.ofDays(props.maxAgeDays))
            .build()

    fun clear(): ResponseCookie =
        base("")
            .maxAge(0)
            .build()

    private fun base(value: String) =
        ResponseCookie.from(props.name, value)
            .httpOnly(true)
            .secure(props.secure)
            .path(props.path)
            .sameSite(props.sameSite)
}