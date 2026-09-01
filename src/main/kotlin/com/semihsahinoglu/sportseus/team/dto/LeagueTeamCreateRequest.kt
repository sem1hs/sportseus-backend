package com.semihsahinoglu.sportseus.team.dto

data class LeagueTeamCreateRequest(
    val leagueExternalId: Int,
    val teamExternalId: Int,
    val season: Int,
)
