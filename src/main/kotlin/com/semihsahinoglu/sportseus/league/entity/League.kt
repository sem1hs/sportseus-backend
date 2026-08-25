package com.semihsahinoglu.sportseus.league.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.dto.LeagueUpdateRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(schema = "league", name = "leagues")
class League(

    @Column(nullable = false)
    var externalId: Int,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: LeagueType,

    @Column(nullable = true)
    var logoUrl: String?,

    @Column(nullable = false)
    var countryName: String,

    @Column(nullable = false)
    var countryCode: String,

    @Column(nullable = false)
    var countryFlag: String,

    @Column(nullable = false)
    var season: Int

) : Auditable() {

    fun updateEntity(request: LeagueUpdateRequest) {
        if (request.name != null) this.name = request.name
        if (request.type != null) this.type = request.type
        if (request.logoUrl != null) this.logoUrl = request.logoUrl
        if (request.countryName != null) this.countryName = request.countryName
        if (request.countryCode != null) this.countryCode = request.countryCode
        if (request.countryFlag != null) this.countryFlag = request.countryFlag
        if (request.season != null) this.season = request.season
    }
}