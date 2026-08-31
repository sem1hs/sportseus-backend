package com.semihsahinoglu.sportseus.venue.dto

data class VenueUpdateRequest(
    val name: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val capacity: Int? = null,
    val surface: String? = null,
    val imageUrl: String? = null,
)
