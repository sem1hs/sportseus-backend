package com.semihsahinoglu.sportseus.standing.repository

import com.semihsahinoglu.sportseus.standing.entity.Standing
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StandingRepository : JpaRepository<Standing, UUID> {

    // Upsert'ün kalbi — (league, team, season)
    fun findByLeagueIdAndTeamIdAndSeason(
        leagueId: UUID,
        teamId: UUID,
        season: Int,
    ): Standing?

    // READ: bir ligin bir sezondaki tam tablosu (rank sırasına göre)
    @EntityGraph(attributePaths = ["league", "team"])
    fun findAllByLeagueExternalIdAndSeasonOrderByRankAsc(
        leagueExternalId: Int,
        season: Int,
    ): List<Standing>

    // READ: bir takımın bir ligdeki sıralaması (tek satır)
    @EntityGraph(attributePaths = ["league", "team"])
    fun findByTeamExternalIdAndLeagueExternalIdAndSeason(
        teamExternalId: Int,
        leagueExternalId: Int,
        season: Int,
    ): Standing?
}