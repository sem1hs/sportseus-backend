package com.semihsahinoglu.sportseus.player.dto.statistic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoalsNode(
    val total: Int? = null,
    val conceded: Int? = null,
    val assists: Int? = null,
    val saves: Int? = null,
)