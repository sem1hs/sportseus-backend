package com.semihsahinoglu.sportseus.player.dto

import java.util.UUID

data class PlayerTeamResponse(
    val id: UUID,
    val season: Int,
    val number: Int?,
    val position: String?,
    val team: PlayerTeamTeamSummary,
)
