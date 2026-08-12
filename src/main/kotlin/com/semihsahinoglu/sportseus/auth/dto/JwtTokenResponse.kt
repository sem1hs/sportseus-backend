package com.semihsahinoglu.sportseus.auth.dto

data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
