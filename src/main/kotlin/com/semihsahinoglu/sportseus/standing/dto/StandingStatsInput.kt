package com.semihsahinoglu.sportseus.standing.dto

import com.semihsahinoglu.sportseus.standing.entity.StandingStats

data class StandingStatsInput(
    val played: Int? = null,
    val win: Int? = null,
    val draw: Int? = null,
    val lose: Int? = null,
    val goalsFor: Int? = null,
    val goalsAgainst: Int? = null,
) {
    fun toStats() = StandingStats(
        played = played,
        win = win,
        draw = draw,
        lose = lose,
        goalsFor = goalsFor,
        goalsAgainst = goalsAgainst,
    )
}
