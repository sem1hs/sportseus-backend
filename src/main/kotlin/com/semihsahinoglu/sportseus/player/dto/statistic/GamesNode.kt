package com.semihsahinoglu.sportseus.player.dto.statistic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GamesNode(
    @JsonProperty("appearences")          // API bunu yanlış yazıyor ("appearences")
    val appearances: Int? = null,

    val lineups: Int? = null,
    val minutes: Int? = null,
    val number: Int? = null,
    val position: String? = null,
    val rating: String? = null,           // "7.792105" — String, Double'a çevirme
    val captain: Boolean? = null,
)