package com.semihsahinoglu.sportseus.standing.dto

import java.util.UUID

data class StandingResponse(
    val id: UUID,
    val season: Int,
    val rank: Int?,
    val points: Int?,
    val goalsDiff: Int?,
    val group: String?,
    val form: String?,
    val status: String?,
    val description: String?,
    val team: StandingTeamSummary,
    val all: StandingStatsDto,
    val home: StandingStatsDto,
    val away: StandingStatsDto,
)