package com.semihsahinoglu.sportseus.transfer.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.team.entity.Team
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity
@Table(
    schema = "transfer",
    name = "transfers",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_transfer_player_date_in_out",
            columnNames = ["player_id", "transfer_date", "team_in_id", "team_out_id"]
        )
    ]
)
class Transfer(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    var player: Player,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_in_id", nullable = false)
    var teamIn: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_out_id", nullable = false)
    var teamOut: Team,

    @Column(name = "transfer_date", nullable = false)
    var date: LocalDate,

    @Column(name = "raw_type", nullable = false, length = 100)
    var rawType: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false, length = 20)
    var transferType: TransferType,

    @Column
    var fee: Long? = null,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    @Column(name = "manual_added", nullable = false)
    var manualAdded: Boolean = false

) : Auditable() {
    fun applyManualUpdate(
        date: LocalDate?,
        rawType: String?,
        transferType: TransferType?,
        fee: Long?,
        teamIn: Team?,
        teamOut: Team?,
    ) {
        teamIn?.let { this.teamIn = it }
        teamOut?.let { this.teamOut = it }
        date?.let { this.date = it }
        rawType?.let { this.rawType = it }
        transferType?.let { this.transferType = it }

        this.fee = when {
            this.transferType == TransferType.SALE -> fee ?: this.fee
            else -> null
        }

        this.manuallyEdited = true
    }
}