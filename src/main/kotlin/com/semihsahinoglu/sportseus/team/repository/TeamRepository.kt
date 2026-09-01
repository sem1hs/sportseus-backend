package com.semihsahinoglu.sportseus.team.repository

import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface TeamRepository : JpaRepository<Team, UUID> {
    fun findByExternalId(externalId: Int): Team?

    // Public okuma: venue'yi de tek sorguda getir (N+1 yok)
    @Query(
        """
        select t from Team t
        left join fetch t.venue
        where t.externalId = :externalId
        """
    )
    fun findByExternalIdWithVenue(@Param("externalId") externalId: Int): Team?
}