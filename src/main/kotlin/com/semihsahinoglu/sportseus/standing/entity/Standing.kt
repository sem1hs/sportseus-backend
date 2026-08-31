package com.semihsahinoglu.sportseus.standing.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.entity.Team
import jakarta.persistence.*

@Entity
@Table(
    schema = "standing",
    name = "standings",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_standing_league_team_season", columnNames = ["league_id", "team_id", "season"])]
)
class Standing(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    var league: League,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @Column(nullable = false)
    var season: Int,

    @Column
    var rank: Int? = null,

    @Column
    var points: Int? = null,

    @Column(name = "goals_diff")
    var goalsDiff: Int? = null,

    @Column(name = "group_name", length = 100)
    var group: String? = null,

    @Column(length = 20)
    var form: String? = null,

    @Column(length = 30)
    var status: String? = null,

    @Column(length = 255)
    var description: String? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "played", column = Column(name = "all_played")),
        AttributeOverride(name = "win", column = Column(name = "all_win")),
        AttributeOverride(name = "draw", column = Column(name = "all_draw")),
        AttributeOverride(name = "lose", column = Column(name = "all_lose")),
        AttributeOverride(name = "goalsFor", column = Column(name = "all_goals_for")),
        AttributeOverride(name = "goalsAgainst", column = Column(name = "all_goals_against")),
    )
    var all: StandingStats = StandingStats(),

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "played", column = Column(name = "home_played")),
        AttributeOverride(name = "win", column = Column(name = "home_win")),
        AttributeOverride(name = "draw", column = Column(name = "home_draw")),
        AttributeOverride(name = "lose", column = Column(name = "home_lose")),
        AttributeOverride(name = "goalsFor", column = Column(name = "home_goals_for")),
        AttributeOverride(name = "goalsAgainst", column = Column(name = "home_goals_against")),
    )
    var home: StandingStats = StandingStats(),

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "played", column = Column(name = "away_played")),
        AttributeOverride(name = "win", column = Column(name = "away_win")),
        AttributeOverride(name = "draw", column = Column(name = "away_draw")),
        AttributeOverride(name = "lose", column = Column(name = "away_lose")),
        AttributeOverride(name = "goalsFor", column = Column(name = "away_goals_for")),
        AttributeOverride(name = "goalsAgainst", column = Column(name = "away_goals_against")),
    )
    var away: StandingStats = StandingStats(),

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    @Column(name = "manual_added", nullable = false)
    var manualAdded: Boolean = false

) : Auditable() {
    fun applyManualUpdate(
        rank: Int?,
        points: Int?,
        goalsDiff: Int?,
        group: String?,
        form: String?,
        status: String?,
        description: String?,
        all: StandingStats?,
        home: StandingStats?,
        away: StandingStats?,
    ) {
        rank?.let { this.rank = it }
        points?.let { this.points = it }
        goalsDiff?.let { this.goalsDiff = it }
        group?.let { this.group = it }
        form?.let { this.form = it }
        status?.let { this.status = it }
        description?.let { this.description = it }
        all?.let {
            val target = this.all ?: StandingStats().also { s -> this.all = s }
            target.merge(it.played, it.win, it.draw, it.lose, it.goalsFor, it.goalsAgainst)
        }
        home?.let {
            val target = this.home ?: StandingStats().also { s -> this.home = s }
            target.merge(it.played, it.win, it.draw, it.lose, it.goalsFor, it.goalsAgainst)
        }
        away?.let {
            val target = this.away ?: StandingStats().also { s -> this.away = s }
            target.merge(it.played, it.win, it.draw, it.lose, it.goalsFor, it.goalsAgainst)
        }
        this.manuallyEdited = true
    }
}