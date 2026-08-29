package com.semihsahinoglu.sportseus.league.dto

import com.semihsahinoglu.sportseus.league.entity.LeagueType

data class LeagueCreateRequest(
    val externalId: Int,
    val name: String,
    val type: LeagueType,
    val logoUrl: String? = null,
    val countryName: String,
    val countryCode: String? = null,
    val countryFlag: String? = null,
    val season: Int,
)
