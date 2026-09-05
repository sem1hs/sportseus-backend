package com.semihsahinoglu.sportseus.transfer.repository

import com.semihsahinoglu.sportseus.transfer.entity.Transfer
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface TransferRepository : JpaRepository<Transfer, UUID> {

    fun findByPlayerIdAndDateAndTeamInIdAndTeamOutId(
        playerId: UUID,
        date: LocalDate,
        teamInId: UUID,
        teamOutId: UUID
    ): Transfer?

    @EntityGraph(attributePaths = ["teamIn", "teamOut"])
    fun findAllByPlayerExternalIdOrderByDateDesc(playerExternalId: Long): List<Transfer>

    @EntityGraph(attributePaths = ["player", "teamIn", "teamOut"])
    @Query(
        """
        SELECT t FROM Transfer t
        WHERE t.teamIn.externalId = :teamExternalId
           OR t.teamOut.externalId = :teamExternalId
        ORDER BY t.date DESC
    """
    )
    fun findAllByTeamExternalId(@Param("teamExternalId") teamExternalId: Int): List<Transfer>

    @EntityGraph(attributePaths = ["player", "teamOut"])
    fun findAllByTeamInExternalIdOrderByDateDesc(teamInExternalId: Int): List<Transfer>

    @EntityGraph(attributePaths = ["player", "teamIn"])
    fun findAllByTeamOutExternalIdOrderByDateDesc(teamOutExternalId: Int): List<Transfer>

    // bir oyuncunun belirli sezon transferleri
    @EntityGraph(attributePaths = ["player", "teamIn", "teamOut"])
    fun findAllByPlayerExternalIdAndSeasonOrderByDateDesc(
        playerExternalId: Long,
        season: Int,
    ): List<Transfer>

    // bir takımın belirli sezon hareketleri (in VEYA out)
    @EntityGraph(attributePaths = ["player", "teamIn", "teamOut"])
    @Query("""
    SELECT t FROM Transfer t
    WHERE t.season = :season
      AND (t.teamIn.externalId = :teamExternalId OR t.teamOut.externalId = :teamExternalId)
    ORDER BY t.date DESC
""")
    fun findAllByTeamExternalIdAndSeason(
        @Param("teamExternalId") teamExternalId: Int,
        @Param("season") season: Int,
    ): List<Transfer>

    // bir sezonun tüm transferleri (opsiyonel — genel liste)
    @EntityGraph(attributePaths = ["player", "teamIn", "teamOut"])
    fun findAllBySeasonOrderByDateDesc(season: Int): List<Transfer>
}