package com.semihsahinoglu.sportseus.lineup.repository

import org.springframework.stereotype.Repository

import com.semihsahinoglu.sportseus.lineup.entity.FixtureLineup
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

@Repository
interface FixtureLineupRepository : JpaRepository<FixtureLineup, UUID> {

    // Upsert'ün kalbi — (fixture, team) ile bul
    fun findByFixtureIdAndTeamId(fixtureId: UUID, teamId: UUID): FixtureLineup?

    // READ: bir maçın iki dizilişi (home + away), oyuncular + team yüklü
    @EntityGraph(attributePaths = ["team", "players"])
    fun findAllByFixtureId(fixtureId: UUID): List<FixtureLineup>

    // READ: tek lineup (fixture + team), oyuncular yüklü
    @EntityGraph(attributePaths = ["team", "players"])
    fun findByFixtureExternalIdAndTeamExternalId(fixtureExternalId: Long, teamExternalId: Int): FixtureLineup?

    // READ: tek lineup (fixture + team), oyuncular yüklü
    @EntityGraph(attributePaths = ["team", "players"])
    fun findByFixtureIdAndTeamExternalId(fixtureId: UUID, teamExternalId: Int): FixtureLineup?
}