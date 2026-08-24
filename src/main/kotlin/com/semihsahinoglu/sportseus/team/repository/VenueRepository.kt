package com.semihsahinoglu.sportseus.team.repository

import com.semihsahinoglu.sportseus.team.entity.Venue
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VenueRepository : JpaRepository<Venue, UUID> {
    fun findByExternalId(externalId: Int): Venue?
}