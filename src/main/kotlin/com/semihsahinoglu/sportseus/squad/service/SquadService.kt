package com.semihsahinoglu.sportseus.squad.service

import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import com.semihsahinoglu.sportseus.squad.dto.SquadResponse
import com.semihsahinoglu.sportseus.squad.mapper.SquadMapper
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SquadService(
    private val playerTeamService: PlayerTeamService,
    private val teamService: TeamService,
    private val squadMapper: SquadMapper
) {

    // PUBLIC: takım listesi döner
    @Transactional(readOnly = true)
    fun getSquadByTeamExternalId(teamExternalId: Int, season: Int): SquadResponse {
        val team = teamService.findByExternalIdOptional(teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: team=$teamExternalId")
        val memberships = playerTeamService.findAllByTeamIdAndSeason(team, season)
        return squadMapper.toResponse(team, season, memberships)
    }
}