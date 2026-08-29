package com.semihsahinoglu.sportseus.standing.dto

data class StandingRowNode(
    val rank: Int? = null,
    val team: StandingTeamNode? = null,
    val points: Int? = null,
    val goalsDiff: Int? = null,
    val group: String? = null,
    val form: String? = null,
    val status: String? = null,
    val description: String? = null,
    val all: StandingStatsNode? = null,
    val home: StandingStatsNode? = null,
    val away: StandingStatsNode? = null,
)
