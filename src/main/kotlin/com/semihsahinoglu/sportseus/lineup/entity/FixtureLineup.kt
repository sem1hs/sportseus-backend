package com.semihsahinoglu.sportseus.lineup.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.team.entity.Team
import jakarta.persistence.*

@Entity
@Table(
    schema = "lineup",
    name = "fixture_lineups",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_lineup_fixture_team", columnNames = ["fixture_id", "team_id"])
    ]
)
class FixtureLineup(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixture_id", nullable = false)
    var fixture: Fixture,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @Column(length = 20)
    var formation: String? = null,

    // coach snapshot (FK yok)
    @Column(name = "coach_external_id")
    var coachExternalId: Int? = null,

    @Column(name = "coach_name", length = 150)
    var coachName: String? = null,

    @Column(name = "coach_photo", columnDefinition = "text")
    var coachPhoto: String? = null,

    @OneToMany(mappedBy = "lineup", cascade = [CascadeType.ALL], orphanRemoval = true)
    var players: MutableList<LineupPlayer> = mutableListOf(),

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    @Column(name = "manual_added", nullable = false)
    var manualAdded: Boolean = false

) : Auditable() {

    fun makeManuallyEdited() {
        this.manuallyEdited = true
    }

    fun addPlayer(player: LineupPlayer) {
        this.players.add(player)
    }
}