package com.semihsahinoglu.sportseus.venue.repository

import com.semihsahinoglu.sportseus.venue.entity.Venue
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface  VenueRepository : JpaRepository<Venue, UUID> {
    fun findByExternalId(externalId: Int): Venue?
}