package com.semihsahinoglu.sportseus.team.dto.statistics

import com.fasterxml.jackson.annotation.JsonProperty

data class TeamStatisticsNode(
    val league: StatLeague? = null,
    val team: StatTeam? = null,
    val form: String? = null,
    val fixtures: StatFixtures? = null,
    val goals: StatGoals? = null,
    val biggest: StatBiggest? = null,

    @JsonProperty("clean_sheet")
    val cleanSheet: HomeAwayTotal? = null,

    @JsonProperty("failed_to_score")
    val failedToScore: HomeAwayTotal? = null,

    val penalty: StatPenalty? = null,
    val lineups: List<StatLineup> = emptyList(),
    val cards: StatCards? = null,
)