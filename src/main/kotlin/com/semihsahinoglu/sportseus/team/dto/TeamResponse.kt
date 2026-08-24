package com.semihsahinoglu.sportseus.team.dto

import java.util.UUID

data class TeamResponse(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val code: String?,
    val country: String?,
    val founded: Int?,
    val national: Boolean,
    val logoUrl: String?,
    val venue: VenueResponse?,       // venue yoksa null
)
