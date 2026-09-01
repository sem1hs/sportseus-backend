package com.semihsahinoglu.sportseus.team.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.venue.entity.Venue
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(schema = "team", name = "teams")
class Team(
    @Column(name = "external_id", nullable = false)
    var externalId: Int,

    @Column(nullable = false, length = 150)
    var name: String,

    @Column(length = 10)
    var code: String? = null,

    @Column(length = 100)
    var country: String? = null,

    var founded: Int? = null,

    @Column(nullable = false)
    var national: Boolean = false,

    @Column(name = "logo_url", length = 500)
    var logoUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")   // nullable — milli takım/venue'siz takım
    var venue: Venue? = null,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false

) : Auditable() {
    fun applyVenue(venue: Venue) {
        this.venue = venue
    }

    fun applyManualUpdate(
        name: String?,
        code: String?,
        country: String?,
        founded: Int?,
        national: Boolean?,
        logoUrl: String?,
    ) {
        name?.let { this.name = it }
        code?.let { this.code = it }
        country?.let { this.country = it }
        founded?.let { this.founded = it }
        national?.let { this.national = it }
        logoUrl?.let { this.logoUrl = it }
        this.manuallyEdited = true
    }
}