package com.semihsahinoglu.sportseus.standing.entity

import jakarta.persistence.Embeddable

@Embeddable
class StandingStats(
    var played: Int? = null,
    var win: Int? = null,
    var draw: Int? = null,
    var lose: Int? = null,
    var goalsFor: Int? = null,
    var goalsAgainst: Int? = null,
) {
    fun merge(played: Int?, win: Int?, draw: Int?, lose: Int?, goalsFor: Int?, goalsAgainst: Int?) {
        played?.let { this.played = it }
        win?.let { this.win = it }
        draw?.let { this.draw = it }
        lose?.let { this.lose = it }
        goalsFor?.let { this.goalsFor = it }
        goalsAgainst?.let { this.goalsAgainst = it }
    }
}