package com.semihsahinoglu.sportseus.player.repository

import com.semihsahinoglu.sportseus.player.entity.PlayerStatistics
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlayerStatisticsRepository : JpaRepository<PlayerStatistics, UUID> {

    // İstatistik upsert'ünün kalbi — bu (player,team,league,season) kaydı var mı?
    fun findByPlayerIdAndTeamIdAndLeagueIdAndSeason(
        playerId: UUID,
        teamId: UUID,
        leagueId: UUID,
        season: Int
    ): PlayerStatistics?

    // Bir oyuncunun belirli sezondaki tüm lig istatistikleri (public okuma)
    fun findAllByPlayerIdAndSeason(playerId: UUID, season: Int): List<PlayerStatistics>

    // Bir oyuncunun tüm istatistikleri
    fun findAllByPlayerId(playerId: UUID): List<PlayerStatistics>

    @EntityGraph(attributePaths = ["team", "league"])
    fun findAllByPlayerExternalIdAndSeason(playerExternalId: Long, season: Int): List<PlayerStatistics>
}