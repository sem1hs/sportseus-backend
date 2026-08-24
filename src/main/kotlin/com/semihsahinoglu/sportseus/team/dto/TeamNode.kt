package com.semihsahinoglu.sportseus.team.dto

data class TeamNode(
    val id: Int,
    val name: String,
    val code: String?,
    val country: String?,
    val founded: Int?,
    val national: Boolean = false,
    val logo: String?,
)
