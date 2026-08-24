package com.semihsahinoglu.sportseus.team.mapper

import com.semihsahinoglu.sportseus.team.dto.TeamNode
import com.semihsahinoglu.sportseus.team.dto.TeamResponse
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.entity.Venue
import org.springframework.stereotype.Component

@Component
class TeamMapper(
    private val venueMapper: VenueMapper
) {
    fun toEntity(node: TeamNode, venue: Venue?): Team =
        Team(
            externalId = node.id,
            name = node.name,
            code = node.code,
            country = node.country,
            founded = node.founded,
            national = node.national,
            logoUrl = node.logo,
            venue = venue,
        )

    fun applyApiData(target: Team, node: TeamNode, venue: Venue?) {
        target.name = node.name
        target.code = node.code
        target.country = node.country
        target.founded = node.founded
        target.national = node.national
        target.logoUrl = node.logo
        target.venue = venue
    }

    fun toResponse(team: Team): TeamResponse =
        TeamResponse(
            id = team.id!!,
            externalId = team.externalId,
            name = team.name,
            code = team.code,
            country = team.country,
            founded = team.founded,
            national = team.national,
            logoUrl = team.logoUrl,
            venue = team.venue?.let(venueMapper::toResponse),   // venue varsa map et
        )
}