package com.semihsahinoglu.sportseus.fixture.dto

import java.time.LocalDateTime

data class FixtureUpdateRequest(
    val leagueExternalId: Int? = null,
    val homeTeamExternalId: Int? = null,
    val awayTeamExternalId: Int? = null,
    val venueExternalId: Int? = null,
    val season: Int? = null,
    val matchDate: LocalDateTime? = null,
    val statusShort: String? = null,
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