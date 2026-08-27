package com.semihsahinoglu.sportseus.fixture.dto

import java.util.UUID

data class FixtureTeamSummary(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val logoUrl: String?,
)
