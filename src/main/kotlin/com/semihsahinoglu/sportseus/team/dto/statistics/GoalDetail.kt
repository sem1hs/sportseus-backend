package com.semihsahinoglu.sportseus.team.dto.statistics

import com.fasterxml.jackson.annotation.JsonProperty

data class GoalDetail(
    val total: HomeAwayTotal? = null,
    val average: HomeAwayTotalStr? = null,
    val minute: Map<String, MinuteBucket> = emptyMap(),
    @JsonProperty("under_over")
    val underOver: Map<String, UnderOver> = emptyMap()
)
