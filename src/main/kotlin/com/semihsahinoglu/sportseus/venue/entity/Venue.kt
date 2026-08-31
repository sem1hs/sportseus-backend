package com.semihsahinoglu.sportseus.venue.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "venue", name = "venues")
class Venue(
    @Column(name = "external_id", nullable = false)
    var externalId: Int,

    @Column(length = 150)
    var name: String? = null,

    @Column(length = 255)
    var address: String? = null,

    @Column(length = 100)
    var city: String? = null,

    @Column(length = 100)
    var country: String? = null,

    var capacity: Int? = null,

    @Column(length = 50)
    var surface: String? = null,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,

    @Column(name = "manually_edited", nullable = false)
    var manuallyEdited: Boolean = false
) : Auditable() {
    fun updateFrom(other: Venue) {
        this.name = other.name
        this.address = other.address
        this.city = other.city
        this.country = other.country
        this.capacity = other.capacity
        this.surface = other.surface
        this.imageUrl = other.imageUrl
    }

    fun applyManualUpdate(
        name: String?,
        address: String?,
        city: String?,
        country: String?,
        capacity: Int?,
        surface: String?,
        imageUrl: String?,
    ) {
        name?.let { this.name = it }
        address?.let { this.address = it }
        city?.let { this.city = it }
        country?.let { this.country = it }
        capacity?.let { this.capacity = it }
        surface?.let { this.surface = it }
        imageUrl?.let { this.imageUrl = it }
        this.manuallyEdited = true
    }
}