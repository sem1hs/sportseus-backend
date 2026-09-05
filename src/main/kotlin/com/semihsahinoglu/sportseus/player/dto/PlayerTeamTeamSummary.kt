package com.semihsahinoglu.sportseus.player.dto

import java.util.UUID

data class PlayerTeamTeamSummary(
    val id: UUID,
    val externalId: Long,
    val name: String,
    val logoUrl: String?,
    val national:Boolean?,
)
