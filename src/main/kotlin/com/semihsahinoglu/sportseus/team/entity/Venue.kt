package com.semihsahinoglu.sportseus.team.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "team", name = "venues")
class Venue(
    @Column(name = "external_id", nullable = false)
    var externalId: Int,

    @Column(length = 150)
    var name: String? = null,

    @Column(length = 255)
    var address: String? = null,

    @Column(length = 100)
    var city: String? = null,

    var capacity: Int? = null,

    @Column(length = 50)
    var surface: String? = null,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,
) : Auditable() {
    fun updateFrom(other: Venue) {
        this.name = other.name
        this.address = other.address
        this.city = other.city
        this.capacity = other.capacity
        this.surface = other.surface
        this.imageUrl = other.imageUrl
    }
}