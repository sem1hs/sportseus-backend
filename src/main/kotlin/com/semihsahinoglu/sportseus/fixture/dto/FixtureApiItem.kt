package com.semihsahinoglu.sportseus.fixture.dto

data class FixtureApiItem(
    val fixture: FixtureNode? = null,
    val league: FixtureLeagueNode? = null,
    val teams: FixtureTeamsNode? = null,
    val goals: FixtureGoalsNode? = null,
    val score: FixtureScoreNode? = null,
)
