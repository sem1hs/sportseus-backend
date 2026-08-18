package com.semihsahinoglu.sportseus.league.dto

data class LeagueNode(
    val id: Int,
    val name: String,
    val type: String,          // "League" | "Cup"
    val logo: String?
)
