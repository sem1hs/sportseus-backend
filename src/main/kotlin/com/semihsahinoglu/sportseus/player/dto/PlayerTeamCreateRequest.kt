package com.semihsahinoglu.sportseus.player.dto

import java.util.UUID

data class PlayerTeamCreateRequest(
    val playerId: UUID,
    val teamExternalId: Int,
    val season: Int,
    val number: Int? = null,
    val position: String? = null,
)
