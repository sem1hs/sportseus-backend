package com.semihsahinoglu.sportseus.team.dto.statistics

import com.fasterxml.jackson.annotation.JsonProperty

data class StatGoals(
    @JsonProperty("for")
    val goalsFor: GoalDetail? = null,
    val against: GoalDetail? = null,
)
