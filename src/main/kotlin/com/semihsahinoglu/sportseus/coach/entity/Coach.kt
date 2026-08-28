package com.semihsahinoglu.sportseus.coach.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    schema = "coach",
    name = "coaches",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_coach_external_id", columnNames = ["external_id"])
    ]
)
class Coach(

    @Column(name = "external_id", nullable = false, unique = true)
    var externalId: Int,

    @Column(nullable = false, length = 150)
    var name: String,

    @Column(name = "first_name", length = 100)
    var firstName: String? = null,

    @Column(name = "last_name", length = 100)
    var lastName: String? = null,

    @Column
    var age: Int? = null,

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Column(name = "birth_place", length = 150)
    var birthPlace: String? = null,

    @Column(name = "birth_country", length = 100)
    var birthCountry: String? = null,

    @Column(length = 100)
    var nationality: String? = null,

    @Column(length = 20)
    var height: String? = null,

    @Column(length = 20)
    var weight: String? = null,

    @Column(columnDefinition = "text")
    var photo: String? = null,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false,

    ) : Auditable() {

    // partial update (transfer deseni) — manuallyEdited=true çağıran set eder
    fun applyManualUpdate(
        name: String?,
        firstName: String?,
        lastName: String?,
        age: Int?,
        birthDate: LocalDate?,
        birthPlace: String?,
        birthCountry: String?,
        nationality: String?,
        height: String?,
        weight: String?,
        photo: String?,
    ) {
        name?.let { this.name = it }
        firstName?.let { this.firstName = it }
        lastName?.let { this.lastName = it }
        age?.let { this.age = it }
        birthDate?.let { this.birthDate = it }
        birthPlace?.let { this.birthPlace = it }
        birthCountry?.let { this.birthCountry = it }
        nationality?.let { this.nationality = it }
        height?.let { this.height = it }
        weight?.let { this.weight = it }
        photo?.let { this.photo = it }
        this.manuallyEdited = true
    }
}