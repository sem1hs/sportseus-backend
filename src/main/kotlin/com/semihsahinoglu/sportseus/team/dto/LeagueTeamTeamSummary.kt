package com.semihsahinoglu.sportseus.team.dto

import java.util.UUID

data class LeagueTeamTeamSummary(
    val id: UUID?,
    val externalId: Int,
    val name: String,
    val logoUrl: String?
)
