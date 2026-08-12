package com.semihsahinoglu.sportseus.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LogoutRequest(
    @field:NotBlank
    val refreshToken: String,

    @field:NotNull
    val allDevices: Boolean
)
