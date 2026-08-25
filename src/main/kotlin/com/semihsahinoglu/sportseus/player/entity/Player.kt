package com.semihsahinoglu.sportseus.player.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(schema = "player", name = "players")
class Player(
    @Column(name = "external_id", nullable = false, unique = true)
    var externalId: Long,                 // API-Football player id

    @Column(nullable = false)
    var name: String,

    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column
    var age: Int? = null,

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Column(name = "birth_place")
    var birthPlace: String? = null,

    @Column(name = "birth_country")
    var birthCountry: String? = null,

    @Column
    var nationality: String? = null,

    @Column
    var height: String? = null,           // "175" — API string veriyor, öyle tut

    @Column
    var weight: String? = null,           // "71"

    @Column(columnDefinition = "text")
    var photo: String? = null
) : Auditable()