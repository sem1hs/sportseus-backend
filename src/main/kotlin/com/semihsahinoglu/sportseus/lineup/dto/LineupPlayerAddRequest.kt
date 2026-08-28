package com.semihsahinoglu.sportseus.lineup.dto

data class LineupPlayerAddRequest(
    val playerExternalId: Int,
    val name: String? = null,
    val number: Int? = null,
    val position: String? = null,
    val isStarter: Boolean,
)
