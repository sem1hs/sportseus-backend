package com.semihsahinoglu.sportseus.venue.dto

data class VenueApiEnvelope(
    val response: List<VenueApiItem> = emptyList(),
)
