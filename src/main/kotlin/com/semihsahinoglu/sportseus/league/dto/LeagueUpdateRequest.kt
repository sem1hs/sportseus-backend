package com.semihsahinoglu.sportseus.league.dto

import com.semihsahinoglu.sportseus.league.entity.LeagueType

data class LeagueUpdateRequest(
    val name: String?,
    val type: LeagueType?,
    val logoUrl: String?,
    val countryName: String?,
    val countryCode: String?,
    val countryFlag: String?,
    val season: Int?,
)
