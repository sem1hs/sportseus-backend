package com.semihsahinoglu.sportseus.player.dto.team

import com.semihsahinoglu.sportseus.player.dto.TeamRefNode

data class PlayerTeamsApiItem(
    val team: TeamRefNode? = null,
    val seasons: List<Int> = emptyList(),   // boş [] gelebilir (Fiorentina)
)