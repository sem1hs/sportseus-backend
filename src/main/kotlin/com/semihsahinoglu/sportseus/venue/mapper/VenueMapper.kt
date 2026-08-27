package com.semihsahinoglu.sportseus.venue.mapper

import com.semihsahinoglu.sportseus.venue.dto.VenueApiItem
import com.semihsahinoglu.sportseus.venue.dto.VenueNode
import com.semihsahinoglu.sportseus.venue.dto.VenueResponse
import com.semihsahinoglu.sportseus.venue.entity.Venue
import org.springframework.stereotype.Component

@Component
class VenueMapper {

    fun toEntity(node: VenueNode): Venue? {
        val externalId = node.id ?: return null
        return Venue(
            externalId = externalId,
            name = node.name,
            address = node.address,
            city = node.city,
            capacity = node.capacity,
            surface = node.surface,
            imageUrl = node.image,
        )
    }

    fun toEntity(item: VenueApiItem): Venue =
        Venue(
            externalId = requireNotNull(item.id) { "Venue id null olamaz" },
            name = item.name,
            address = item.address,
            city = item.city,
            country = item.country,
            capacity = item.capacity,
            surface = item.surface,
            imageUrl = item.image,
        )

    fun applyApiData(target: Venue, item: VenueApiItem) {
        target.name = item.name
        target.address = item.address
        target.city = item.city
        target.country = item.country
        target.capacity = item.capacity
        target.surface = item.surface
        target.imageUrl = item.image
    }

    fun applyApiData(target: Venue, node: VenueNode) {
        target.name = node.name
        target.address = node.address
        target.city = node.city
        target.capacity = node.capacity
        target.surface = node.surface
        target.imageUrl = node.image
    }

    fun toResponse(venue: Venue): VenueResponse =
        VenueResponse(
            id = venue.id!!,
            externalId = venue.externalId,
            name = venue.name,
            address = venue.address,
            city = venue.city,
            country = venue.country,
            capacity = venue.capacity,
            surface = venue.surface,
            imageUrl = venue.imageUrl,
        )
}