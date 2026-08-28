package com.semihsahinoglu.sportseus.lineup.dto

import java.util.UUID

data class LineupTeamSummary(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val logoUrl: String?,
)
