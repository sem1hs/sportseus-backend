package com.semihsahinoglu.sportseus.player.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.entity.Team
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes


@Entity
@Table(
    schema = "player",
    name = "player_statistics",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_player_team_league_season",
            columnNames = ["player_id", "team_id", "league_id", "season"]
        )
    ]
)
class PlayerStatistics(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    var player: Player,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    var league: League,

    @Column(nullable = false)
    var season: Int,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    var stats: String,                    // saf istatistik jsonb (id'siz)
) : Auditable()