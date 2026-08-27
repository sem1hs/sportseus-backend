package com.semihsahinoglu.sportseus.fixture.dto

data class FixtureScoreDto(
    val halftime: ScorePairDto,
    val fulltime: ScorePairDto,
    val extratime: ScorePairDto,
    val penalty: ScorePairDto,
)
