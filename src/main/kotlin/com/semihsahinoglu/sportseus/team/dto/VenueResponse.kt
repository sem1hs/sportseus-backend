package com.semihsahinoglu.sportseus.team.dto

import java.util.UUID

data class VenueResponse(
    val id: UUID,
    val externalId: Int,
    val name: String?,
    val address: String?,
    val city: String?,
    val capacity: Int?,
    val surface: String?,
    val imageUrl: String?,
)
