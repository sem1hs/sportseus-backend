package com.semihsahinoglu.sportseus.auth.dto

import java.time.Instant
import java.util.UUID

data class RefreshTokenData(
    val userId: UUID,
    val tokenHash: String,
    val issuedAt: Instant,
    val lastUsedAt: Instant,
    val expiresAt: Instant,
)
