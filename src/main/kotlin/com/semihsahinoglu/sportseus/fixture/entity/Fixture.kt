package com.semihsahinoglu.sportseus.fixture.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.venue.entity.Venue
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    schema = "fixture",
    name = "fixtures",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_fixture_external_id", columnNames = ["external_id"])
    ]
)
class Fixture(

    @Column(name = "external_id", nullable = false, unique = true)
    var externalId: Long,

    @Column(nullable = false)
    var season: Int,

    @Column(name = "match_date", nullable = false)
    var matchDate: LocalDateTime,

    @Column(name = "timestamp_epoch")
    var timestampEpoch: Long? = null,

    @Column(name = "status_short", nullable = false, length = 10)
    var statusShort: String,

    @Column(name = "status_long", length = 50)
    var statusLong: String? = null,

    @Column
    var elapsed: Int? = null,

    @Column
    var extra: Int? = null,

    @Column(length = 100)
    var round: String? = null,

    @Column(length = 150)
    var referee: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    var league: League,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    var homeTeam: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    var awayTeam: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    var venue: Venue? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "home", column = Column(name = "goals_home")),
        AttributeOverride(name = "away", column = Column(name = "goals_away")),
    )
    var goals: ScorePair? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "home", column = Column(name = "ht_home")),
        AttributeOverride(name = "away", column = Column(name = "ht_away")),
    )
    var halftime: ScorePair? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "home", column = Column(name = "ft_home")),
        AttributeOverride(name = "away", column = Column(name = "ft_away")),
    )
    var fulltime: ScorePair? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "home", column = Column(name = "et_home")),
        AttributeOverride(name = "away", column = Column(name = "et_away")),
    )
    var extratime: ScorePair? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "home", column = Column(name = "pen_home")),
        AttributeOverride(name = "away", column = Column(name = "pen_away")),
    )
    var penalty: ScorePair? = null,

    @Column(name = "home_winner")
    var homeWinner: Boolean? = null,

    @Column(name = "away_winner")
    var awayWinner: Boolean? = null

) : Auditable() {
    fun applyVenue(venue: Venue) {
        this.venue = venue
    }
}