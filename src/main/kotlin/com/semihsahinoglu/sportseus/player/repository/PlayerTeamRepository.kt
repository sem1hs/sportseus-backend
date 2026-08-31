package com.semihsahinoglu.sportseus.player.repository

import com.semihsahinoglu.sportseus.player.entity.PlayerTeam
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlayerTeamRepository : JpaRepository<PlayerTeam, UUID> {

    // ensurePlayerTeam upsert'ünün kalbi — üyelik var mı?
    fun findByPlayerIdAndTeamIdAndSeason(playerId: UUID, teamId: UUID, season: Int): PlayerTeam?

    // Bir takımın belirli sezondaki kadrosu
    @EntityGraph(attributePaths = ["player"])
    fun findAllByTeamIdAndSeason(teamId: UUID, season: Int): List<PlayerTeam>

    // Bir oyuncunun tüm üyelik geçmişi
    fun findAllByPlayerId(playerId: UUID): List<PlayerTeam>

    @EntityGraph(attributePaths = ["team"])
    fun findAllByPlayerExternalIdOrderBySeasonDesc(playerExternalId: Long): List<PlayerTeam>

    @EntityGraph(attributePaths = ["team"])
    fun findAllByPlayerIdOrderBySeasonDesc(playerId: UUID): List<PlayerTeam>
}