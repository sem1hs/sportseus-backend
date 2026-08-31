package com.semihsahinoglu.sportseus.squad.dto

data class SquadResponse(
    val season: Int,
    val team: SquadTeamSummary,
    val players: List<SquadPlayerItem>,
)
