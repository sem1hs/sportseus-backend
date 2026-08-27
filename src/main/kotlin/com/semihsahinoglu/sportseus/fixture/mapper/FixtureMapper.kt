package com.semihsahinoglu.sportseus.fixture.mapper

import com.semihsahinoglu.sportseus.fixture.dto.FixtureApiItem
import com.semihsahinoglu.sportseus.fixture.dto.FixtureLeagueSummary
import com.semihsahinoglu.sportseus.fixture.dto.FixtureResponse
import com.semihsahinoglu.sportseus.fixture.dto.FixtureScoreDto
import com.semihsahinoglu.sportseus.fixture.dto.FixtureTeamSummary
import com.semihsahinoglu.sportseus.fixture.dto.FixtureVenueSummary
import com.semihsahinoglu.sportseus.fixture.dto.ScorePairDto
import com.semihsahinoglu.sportseus.fixture.dto.ScorePairNode
import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.fixture.entity.ScorePair
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.venue.entity.Venue
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class FixtureMapper {

    fun toEntity(
        item: FixtureApiItem,
        season: Int,
        league: League,
        homeTeam: Team,
        awayTeam: Team,
        venue: Venue?
    ): Fixture {
        val fx = item.fixture!!
        val teams = item.teams

        return Fixture(
            externalId = fx.id!!,
            season = season,                        // parametre-otoriter değil; league.season kullanıyoruz (aşağıda not)
            matchDate = fx.date!!.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime(),
            timestampEpoch = fx.timestamp,
            statusShort = fx.status?.short ?: "NS",
            statusLong = fx.status?.long,
            elapsed = fx.status?.elapsed,
            extra = fx.status?.extra,
            round = item.league?.round,
            referee = fx.referee,
            league = league,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            venue = venue,
            goals = ScorePair(item.goals?.home, item.goals?.away),
            halftime = item.score?.halftime.toScorePair(),
            fulltime = item.score?.fulltime.toScorePair(),
            extratime = item.score?.extratime.toScorePair(),
            penalty = item.score?.penalty.toScorePair(),
            homeWinner = teams?.home?.winner,
            awayWinner = teams?.away?.winner,
        )
    }

    fun applyApiData(target: Fixture, item: FixtureApiItem, season: Int, venue: Venue?) {
        val fx = item.fixture!!
        val teams = item.teams

        target.season = season
        target.matchDate = fx.date?.withOffsetSameInstant(ZoneOffset.UTC)?.toLocalDateTime() ?: target.matchDate
        target.timestampEpoch = fx.timestamp
        target.statusShort = fx.status?.short ?: target.statusShort
        target.statusLong = fx.status?.long
        target.elapsed = fx.status?.elapsed
        target.extra = fx.status?.extra
        target.round = item.league?.round
        target.referee = fx.referee
        venue?.let { target.venue = it }

        target.goals = ScorePair(item.goals?.home, item.goals?.away)
        target.halftime = item.score?.halftime.toScorePair()
        target.fulltime = item.score?.fulltime.toScorePair()
        target.extratime = item.score?.extratime.toScorePair()
        target.penalty = item.score?.penalty.toScorePair()

        target.homeWinner = teams?.home?.winner
        target.awayWinner = teams?.away?.winner
    }

    fun toResponse(f: Fixture): FixtureResponse =
        FixtureResponse(
            id = f.id!!,
            externalId = f.externalId,
            season = f.season,
            date = f.matchDate,
            statusShort = f.statusShort,
            statusLong = f.statusLong,
            elapsed = f.elapsed,
            extra = f.extra,
            round = f.round,
            referee = f.referee,
            league = FixtureLeagueSummary(
                id = f.league.id!!,
                externalId = f.league.externalId,
                name = f.league.name,
                logoUrl = f.league.logoUrl,
            ),
            venue = f.venue?.let { v ->
                FixtureVenueSummary(
                    id = v.id!!,
                    externalId = v.externalId,
                    name = v.name,
                    city = v.city,
                )
            },
            homeTeam = f.homeTeam.toTeamSummary(),
            awayTeam = f.awayTeam.toTeamSummary(),
            homeWinner = f.homeWinner,
            awayWinner = f.awayWinner,
            goals = f.goals.toDto(),
            score = FixtureScoreDto(
                halftime = f.halftime.toDto(),
                fulltime = f.fulltime.toDto(),
                extratime = f.extratime.toDto(),
                penalty = f.penalty.toDto(),
            ),
        )

    private fun ScorePairNode?.toScorePair(): ScorePair =
        ScorePair(this?.home, this?.away)

    private fun ScorePair?.toDto(): ScorePairDto =
        ScorePairDto(this?.home, this?.away)

    private fun Team.toTeamSummary(): FixtureTeamSummary =
        FixtureTeamSummary(
            id = this.id!!,
            externalId = this.externalId,
            name = this.name,
            logoUrl = this.logoUrl,
        )
}