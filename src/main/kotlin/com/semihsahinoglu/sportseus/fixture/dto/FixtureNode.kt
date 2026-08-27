package com.semihsahinoglu.sportseus.fixture.dto

import java.time.OffsetDateTime

data class FixtureNode(
    val id: Long? = null,
    val referee: String? = null,
    val timezone: String? = null,
    val date: OffsetDateTime? = null,   // "2024-08-11T16:15:00+00:00"
    val timestamp: Long? = null,
    val venue: FixtureVenueNode? = null,
    val status: FixtureStatusNode? = null,
)
