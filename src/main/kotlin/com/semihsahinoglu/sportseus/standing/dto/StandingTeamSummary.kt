package com.semihsahinoglu.sportseus.standing.dto

import java.util.UUID

data class StandingTeamSummary(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val logoUrl: String?,
)
