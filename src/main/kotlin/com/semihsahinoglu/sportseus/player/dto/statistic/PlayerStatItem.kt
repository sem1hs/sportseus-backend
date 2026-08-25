package com.semihsahinoglu.sportseus.player.dto.statistic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.semihsahinoglu.sportseus.player.dto.LeagueRefNode
import com.semihsahinoglu.sportseus.player.dto.TeamRefNode

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlayerStatItem(
    val team: TeamRefNode? = null,       // resolve için, jsonb'ye girmez
    val league: LeagueRefNode? = null,   // resolve için, jsonb'ye girmez
    val games: GamesNode? = null,
    val goals: GoalsNode? = null,
)