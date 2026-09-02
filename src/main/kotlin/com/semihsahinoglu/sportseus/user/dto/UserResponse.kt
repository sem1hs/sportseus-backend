package com.semihsahinoglu.sportseus.user.dto

import com.semihsahinoglu.sportseus.user.entity.Role

data class UserResponse(
    val email: String,
    val displayName: String,
    val role: Role
)