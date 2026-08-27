package com.semihsahinoglu.sportseus.fixture.dto

data class FixtureScoreNode(
    val halftime: ScorePairNode? = null,
    val fulltime: ScorePairNode? = null,
    val extratime: ScorePairNode? = null,
    val penalty: ScorePairNode? = null,
)
