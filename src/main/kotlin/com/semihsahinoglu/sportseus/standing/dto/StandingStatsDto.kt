package com.semihsahinoglu.sportseus.standing.dto

data class StandingStatsDto(
    val played: Int?,
    val win: Int?,
    val draw: Int?,
    val lose: Int?,
    val goalsFor: Int?,
    val goalsAgainst: Int?
)
