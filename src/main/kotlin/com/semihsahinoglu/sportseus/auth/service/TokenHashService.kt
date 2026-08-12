package com.semihsahinoglu.sportseus.auth.service

import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.HexFormat

@Service
class TokenHashService {

    fun hash(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(token.toByteArray(StandardCharsets.UTF_8))
        return HexFormat.of().formatHex(bytes)
    }
}