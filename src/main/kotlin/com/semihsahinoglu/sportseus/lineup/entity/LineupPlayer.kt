package com.semihsahinoglu.sportseus.lineup.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerUpdateRequest
import jakarta.persistence.*

@Entity
@Table(schema = "lineup", name = "lineup_players")
class LineupPlayer(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineup_id", nullable = false)
    var lineup: FixtureLineup,

    @Column(name = "player_external_id", nullable = false)
    var playerExternalId: Int,

    @Column(name = "player_name", length = 150)
    var playerName: String? = null,

    @Column(name = "shirt_number")
    var number: Int? = null,

    @Column(length = 5)
    var position: String? = null,        // "G"/"D"/"M"/"F"

    @Column(name = "is_starter", nullable = false)
    var isStarter: Boolean

) : Auditable() {

    fun updateLineupPlayer(request: LineupPlayerUpdateRequest) {
        request.number?.let { this.number = it }
        request.position?.let { this.position = it }
        request.isStarter?.let { this.isStarter = it }
    }
}