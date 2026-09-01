package com.semihsahinoglu.sportseus.team.dto.statistics

import java.util.UUID

data class StatTeamSummary(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val logoUrl: String?,
)
