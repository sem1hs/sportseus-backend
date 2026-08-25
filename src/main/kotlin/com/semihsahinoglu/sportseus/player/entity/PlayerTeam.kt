package com.semihsahinoglu.sportseus.player.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.team.entity.Team
import jakarta.persistence.*

@Entity
@Table(
    schema = "player",
    name = "player_teams",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_player_team_season",
            columnNames = ["player_id", "team_id", "season"]
        )
    ]
)
class PlayerTeam(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    var player: Player,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @Column(nullable = false)
    var season: Int,

    @Column(name = "shirt_number")
    var number: Int? = null,              // squad doldurur, /teams null bırakır

    @Column
    var position: String? = null,
) : Auditable()