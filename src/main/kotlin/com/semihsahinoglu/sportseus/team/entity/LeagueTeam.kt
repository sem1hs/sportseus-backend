package com.semihsahinoglu.sportseus.team.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.entity.League
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    schema = "team",
    name = "league_teams",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_league_team_season", columnNames = ["league_id", "team_id", "season"])
    ]
)
class LeagueTeam(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    var league: League,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @Column(nullable = false)
    var season: Int,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    @Column(name = "manual_added", nullable = false)
    var manualAdded: Boolean = false
) : Auditable() {
    fun applyManualUpdate(
        league: League?,
        team: Team?,
        season: Int?
    ) {
        league?.let { this.league = it }
        team?.let { this.team = it }
        season?.let { this.season = it }
        this.manuallyEdited = true
    }
}