package com.semihsahinoglu.sportseus.league.dto

data class LeagueByTeamApiItem(
    val league: LeagueNode? = null,      // { id, name, type, logo } — mevcut LeagueNode kullanılabilir
    val country: CountryNode? = null,    // { name, code, flag }
    val seasons: List<LeagueSeasonNode> = emptyList(),
)
