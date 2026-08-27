package com.semihsahinoglu.sportseus.team.dto

import com.semihsahinoglu.sportseus.venue.dto.VenueNode

data class TeamApiItem(
    val team: TeamNode,
    val venue: VenueNode?,        // milli takım/venue'siz → null gelebilir
)
