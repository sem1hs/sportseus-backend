package com.semihsahinoglu.sportseus.player.dto

import java.util.UUID

data class SquadTeamSummary(
    val id: UUID,
    val externalId: Long,
    val name: String,
    val logoUrl: String?,
)