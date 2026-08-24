package com.semihsahinoglu.sportseus.team.mapper

import com.semihsahinoglu.sportseus.team.dto.VenueNode
import com.semihsahinoglu.sportseus.team.dto.VenueResponse
import com.semihsahinoglu.sportseus.team.entity.Venue
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
            capacity = venue.capacity,
            surface = venue.surface,
            imageUrl = venue.imageUrl,
        )
}