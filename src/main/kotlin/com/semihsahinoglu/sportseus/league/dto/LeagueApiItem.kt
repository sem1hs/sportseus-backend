package com.semihsahinoglu.sportseus.league.dto

data class LeagueApiItem(
    val league: LeagueNode,
    val country: CountryNode,
    val seasons: List<SeasonNode> = emptyList(),
)
