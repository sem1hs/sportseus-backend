package com.semihsahinoglu.sportseus.player.dto

import java.util.UUID

data class PlayerTeamUpdateRequest(
    val playerId: UUID? = null,
    val teamExternalId: Int? = null,
    val season: Int? = null,
    val number: Int? = null,
    val position: String? = null,
)
