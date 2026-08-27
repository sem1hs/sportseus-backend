package com.semihsahinoglu.sportseus.fixture.dto

import java.util.UUID

data class FixtureVenueSummary(
    val id: UUID,
    val externalId: Int,
    val name: String?,
    val city: String?,
)
