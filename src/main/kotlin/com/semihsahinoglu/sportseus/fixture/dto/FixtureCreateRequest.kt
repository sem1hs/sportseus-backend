package com.semihsahinoglu.sportseus.fixture.dto

import java.time.LocalDateTime

data class FixtureCreateRequest(
    val leagueExternalId: Int,
    val homeTeamExternalId: Int,
    val awayTeamExternalId: Int,
    val venueExternalId: Int? = null,
    val season: Int,
    val matchDate: LocalDateTime,
    val statusShort: String = "NS",
    val statusLong: String? = null,
    val elapsed: Int? = null,
    val extra: Int? = null,
    val round: String? = null,
    val referee: String? = null,
    val homeWinner: Boolean? = null,
    val awayWinner: Boolean? = null,
    val goals: ScorePairInput? = null,
    val halftime: ScorePairInput? = null,
    val fulltime: ScorePairInput? = null,
    val extratime: ScorePairInput? = null,
    val penalty: ScorePairInput? = null,
)
