package com.semihsahinoglu.sportseus.team.dto

data class TeamApiItem(
    val team: TeamNode,
    val venue: VenueNode?,        // milli takım/venue'siz → null gelebilir
)
