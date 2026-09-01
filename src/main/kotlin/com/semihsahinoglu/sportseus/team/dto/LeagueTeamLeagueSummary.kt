package com.semihsahinoglu.sportseus.team.dto

import java.util.UUID

data class LeagueTeamLeagueSummary(
    val id: UUID?,
    val externalId: Int,
    val name: String,
    val logoUrl: String?
)
