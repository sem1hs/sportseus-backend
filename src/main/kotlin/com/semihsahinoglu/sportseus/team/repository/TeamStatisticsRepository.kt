package com.semihsahinoglu.sportseus.team.repository

import com.semihsahinoglu.sportseus.team.entity.TeamStatistics
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeamStatisticsRepository : JpaRepository<TeamStatistics, UUID> {
    // Upsert + okuma anahtarı: (takım, lig, sezon) → tek kayıt
    fun findByTeamIdAndLeagueIdAndSeason(teamId: UUID, leagueId: UUID, season: Int): TeamStatistics?
}