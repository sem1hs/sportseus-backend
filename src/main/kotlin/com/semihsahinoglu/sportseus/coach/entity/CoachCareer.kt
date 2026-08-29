package com.semihsahinoglu.sportseus.coach.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    schema = "coach",
    name = "coach_careers",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_coach_career",
            columnNames = ["coach_id", "team_external_id", "start_date"]
        )
    ]
)
class CoachCareer(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    var coach: Coach,

    @Column(name = "team_external_id", nullable = false)
    var teamExternalId: Int,

    @Column(name = "team_name", nullable = false, length = 150)
    var teamName: String,

    @Column(name = "team_logo", columnDefinition = "text")
    var teamLogo: String? = null,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,

    @Column(name = "end_date")
    var endDate: LocalDate? = null,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    @Column(name = "manual_added", nullable = false)
    var manualAdded: Boolean = false

) : Auditable() {
    fun applyUpdate(
        teamExternalId: Int?,
        teamName: String?,
        teamLogo: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ) {
        teamExternalId?.let { this.teamExternalId = it }
        teamName?.let { this.teamName = it }
        teamLogo?.let { this.teamLogo = it }
        startDate?.let { this.startDate = it }
        endDate?.let { this.endDate = it }
        this.manuallyEdited = true
    }
}