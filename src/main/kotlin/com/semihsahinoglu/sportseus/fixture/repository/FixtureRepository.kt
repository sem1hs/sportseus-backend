package com.semihsahinoglu.sportseus.fixture.repository

import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FixtureRepository : JpaRepository<Fixture, UUID> {

    fun findByExternalId(externalId: Long): Fixture?

    @EntityGraph(attributePaths = ["league", "homeTeam", "awayTeam", "venue"])
    fun findAllByLeagueExternalIdAndSeasonOrderByMatchDateAsc(
        leagueExternalId: Int,
        season: Int,
    ): List<Fixture>

    @EntityGraph(attributePaths = ["league", "homeTeam", "awayTeam", "venue"])
    @Query(
        """
        SELECT f FROM Fixture f
        WHERE f.season = :season
          AND (f.homeTeam.externalId = :teamExternalId OR f.awayTeam.externalId = :teamExternalId)
        ORDER BY f.matchDate ASC
    """
    )
    fun findAllByTeamExternalIdAndSeason(
        @Param("teamExternalId") teamExternalId: Int,
        @Param("season") season: Int,
    ): List<Fixture>

    @EntityGraph(attributePaths = ["league", "homeTeam", "awayTeam", "venue"])
    fun findWithRelationsByExternalId(externalId: Long): Fixture?
}