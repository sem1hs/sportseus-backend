package com.semihsahinoglu.sportseus.team.dto.statistics

import com.fasterxml.jackson.annotation.JsonProperty

data class BiggestGoals(
    @JsonProperty("for")
    val goalsFor: HomeAway? = null,

    val against: HomeAway? = null
)
