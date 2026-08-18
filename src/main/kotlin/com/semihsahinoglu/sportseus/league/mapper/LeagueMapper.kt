package com.semihsahinoglu.sportseus.league.mapper

import com.semihsahinoglu.sportseus.league.dto.LeagueApiItem
import com.semihsahinoglu.sportseus.league.dto.LeagueResponse
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.entity.LeagueType
import org.springframework.stereotype.Component

@Component
class LeagueMapper {

    fun toEntity(item: LeagueApiItem, season: Int): League {
        return League(
            externalId = item.league.id,
            name = item.league.name,
            type = toTypeUppercase(item.league.type),
            logoUrl = item.league.logo,
            countryName = item.country.name,
            countryCode = item.country.code ?: "XX",   // NOT NULL kolon için güvenli varsayılan
            countryFlag = item.country.flag ?: "",
            season = season,
        )
    }

    fun toResponse(league: League): LeagueResponse {
        val id = checkNotNull(league.id) { "League id cannot be null" }
        return LeagueResponse(
            id = id,
            externalId = league.externalId,
            name = league.name,
            type = toTypeUppercase(league.type.name),
            logoUrl = league.logoUrl,
            countryName = league.countryName,
            countryCode = league.countryCode,
            countryFlag = league.countryFlag,
            season = league.season,
        )

    }

    private fun toTypeUppercase(type: String): LeagueType =
        when (type.uppercase()) {
            "LEAGUE" -> LeagueType.LEAGUE
            "CUP" -> LeagueType.CUP
            else -> LeagueType.LEAGUE   // beklenmedik tip → varsayılan (log'lanabilir)
        }
}