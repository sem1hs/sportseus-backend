package com.semihsahinoglu.sportseus.fixture.dto

import java.time.LocalDateTime
import java.util.UUID

data class FixtureResponse(
    val id: UUID,
    val externalId: Long?,
    val season: Int,
    val date: LocalDateTime,
    val statusShort: String,
    val statusLong: String?,
    val elapsed: Int?,
    val extra: Int?,
    val round: String?,
    val referee: String?,
    val league: FixtureLeagueSummary,
    val venue: FixtureVenueSummary?,
    val homeTeam: FixtureTeamSummary,
    val awayTeam: FixtureTeamSummary,
    val homeWinner: Boolean?,
    val awayWinner: Boolean?,
    val goals: ScorePairDto,
    val score: FixtureScoreDto,
    val manuallyEdited: Boolean,
    val manualAdded: Boolean,
)
