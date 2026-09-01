package com.semihsahinoglu.sportseus.team.dto

data class LeagueTeamUpdateRequest(
    val leagueExternalId: Int? = null,
    val teamExternalId: Int? = null,
    val season: Int? = null,
)
