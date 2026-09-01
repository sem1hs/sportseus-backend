package com.semihsahinoglu.sportseus.team.dto

data class TeamUpdateRequest(
    val name: String? = null,
    val code: String? = null,
    val country: String? = null,
    val founded: Int? = null,
    val national: Boolean? = null,
    val logoUrl: String? = null
)
