package com.semihsahinoglu.sportseus.league.repository

import com.semihsahinoglu.sportseus.league.entity.League
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LeagueRepository : JpaRepository<League, UUID> {

    fun findByExternalIdAndSeason(externalId: Int, season: Int): League?

    fun findBySeason(season: Int): List<League>
}