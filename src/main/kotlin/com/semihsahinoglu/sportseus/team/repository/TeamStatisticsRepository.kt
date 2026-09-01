package com.semihsahinoglu.sportseus.team.repository

import com.semihsahinoglu.sportseus.team.entity.TeamStatistics
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeamStatisticsRepository : JpaRepository<TeamStatistics, UUID> {
    // Upsert + okuma anahtarı: (takım, lig, sezon) → tek kayıt
    @EntityGraph(attributePaths = ["team", "league"])
    fun findByTeamIdAndLeagueIdAndSeason(teamId: UUID, leagueId: UUID, season: Int): TeamStatistics?

    @EntityGraph(attributePaths = ["team", "league"])
    fun findByTeamIdAndSeason(teamId: UUID, season: Int): TeamStatistics?
}