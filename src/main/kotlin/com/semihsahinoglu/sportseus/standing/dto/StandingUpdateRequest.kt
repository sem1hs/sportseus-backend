package com.semihsahinoglu.sportseus.standing.dto

data class StandingUpdateRequest(
    val rank: Int? = null,
    val points: Int? = null,
    val goalsDiff: Int? = null,
    val group: String? = null,
    val form: String? = null,
    val status: String? = null,
    val description: String? = null,
    val all: StandingStatsInput? = null,
    val home: StandingStatsInput? = null,
    val away: StandingStatsInput? = null,
)