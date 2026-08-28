package com.semihsahinoglu.sportseus.lineup.dto

data class LineupPlayerUpdateRequest(
    val number: Int? = null,
    val position: String? = null,
    val isStarter: Boolean? = null,
)
