package com.semihsahinoglu.sportseus.team.dto

data class VenueNode(
    val id: Int?,                 // venue varsa dolu; nadiren null olabilir
    val name: String?,
    val address: String?,
    val city: String?,
    val capacity: Int?,
    val surface: String?,
    val image: String?,
)
