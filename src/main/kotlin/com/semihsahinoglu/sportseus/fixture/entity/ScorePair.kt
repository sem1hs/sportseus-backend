package com.semihsahinoglu.sportseus.fixture.entity

import jakarta.persistence.Embeddable

@Embeddable
class ScorePair(
    var home: Int? = null,
    var away: Int? = null,
)