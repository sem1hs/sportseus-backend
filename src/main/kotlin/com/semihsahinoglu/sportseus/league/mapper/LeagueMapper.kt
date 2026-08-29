package com.semihsahinoglu.sportseus.league.mapper

import com.semihsahinoglu.sportseus.league.dto.CountryNode
import com.semihsahinoglu.sportseus.league.dto.LeagueApiItem
import com.semihsahinoglu.sportseus.league.dto.LeagueCreateRequest
import com.semihsahinoglu.sportseus.league.dto.LeagueNode
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

    fun toEntity(league: LeagueNode, country: CountryNode?, season: Int): League =
        League(
            externalId = league.id,
            name = league.name,
            type = toTypeUppercase(league.type),
            logoUrl = league.logo,
            countryName = country?.name ?: "",
            countryCode = country?.code ?: "XX",
            countryFlag = country?.flag ?: "",
            season = season,
        )

    fun toEntity(request: LeagueCreateRequest) = League(
        externalId = request.externalId,
        name = request.name,
        type = request.type,
        logoUrl = request.logoUrl,
        countryName = request.countryName,
        countryCode = request.countryCode,
        countryFlag = request.countryFlag,
        season = request.season,
        manualAdded = true,
        manuallyEdited = false,
    )

    fun applyApiData(target: League, league: LeagueNode, country: CountryNode?) {
        target.name = league.name
        target.type = toTypeUppercase(league.type)
        target.logoUrl = league.logo
        target.countryName = country?.name ?: ""
        target.countryCode = country?.code ?: "XX"
        target.countryFlag = country?.flag ?: ""
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
            countryCode = league.countryCode ?: "XX",
            countryFlag = league.countryFlag ?: "",
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