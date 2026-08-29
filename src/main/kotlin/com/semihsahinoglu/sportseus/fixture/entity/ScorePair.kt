package com.semihsahinoglu.sportseus.fixture.entity

import jakarta.persistence.Embeddable

@Embeddable
class ScorePair(
    var home: Int? = null,
    var away: Int? = null,
) {
    fun merge(home: Int?, away: Int?) {
        home?.let { this.home = it }
        away?.let { this.away = it }
    }
}