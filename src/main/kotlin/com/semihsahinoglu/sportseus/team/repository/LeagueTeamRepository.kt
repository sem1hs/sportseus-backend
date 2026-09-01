package com.semihsahinoglu.sportseus.team.repository

import com.semihsahinoglu.sportseus.team.entity.LeagueTeam
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface LeagueTeamRepository : JpaRepository<LeagueTeam, UUID> {

    // Upsert için: bu (lig, takım, sezon) ilişkisi zaten var mı?
    // Duplicate league_teams satırı oluşmasın diye
    @EntityGraph(attributePaths = ["team", "league"])
    fun findByLeagueIdAndTeamIdAndSeason(leagueId: UUID, teamId: UUID, season: Int): LeagueTeam?

    @EntityGraph(attributePaths = ["team", "league"])
    fun findAllByLeagueIdAndSeason(leagueId: UUID, season: Int): List<LeagueTeam>

    // "Bu ligin bu sezondaki takımları" — public okuma ucu için.
    @Query(
        """
        select lt.team from LeagueTeam lt
        join lt.team t
        left join fetch t.venue
        where lt.league.id = :leagueId and lt.season = :season
        """
    )
    fun findTeamsByLeagueIdAndSeason(@Param("leagueId") leagueId: UUID, @Param("season") season: Int): List<LeagueTeam>
}