package com.semihsahinoglu.sportseus.team.service

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.exception.LeagueNotFoundException
import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamCreateRequest
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamResponse
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamUpdateRequest
import com.semihsahinoglu.sportseus.team.entity.LeagueTeam
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.exception.LeagueTeamConflictException
import com.semihsahinoglu.sportseus.team.exception.LeagueTeamNotFoundException
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.mapper.LeagueTeamMapper
import com.semihsahinoglu.sportseus.team.repository.LeagueTeamRepository
import com.semihsahinoglu.sportseus.team.repository.TeamRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LeagueTeamService(
    private val leagueService: LeagueService,
    private val teamRepository: TeamRepository,
    private val leagueTeamRepository: LeagueTeamRepository,
    private val leagueTeamMapper: LeagueTeamMapper
) {

    // ADMIN: elle lig-takım ilişkisi ekle (league+team external, çakışma 409)
    @Transactional
    fun createLeagueTeam(request: LeagueTeamCreateRequest): LeagueTeamResponse {
        val league = leagueService.getByExternalIdAndSeasonEntity(request.leagueExternalId, request.season)
        val team = teamRepository.findByExternalId(request.teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: ${request.teamExternalId}")

        // (league, team, season) çakışma
        val exists =
            leagueTeamRepository.findByLeagueIdAndTeamIdAndSeason(league.id!!, team.id!!, request.season) != null
        if (exists) throw LeagueTeamConflictException("Bu lig-takım ilişkisi zaten var: league=${request.leagueExternalId} team=${request.teamExternalId} season=${request.season}")

        val leagueTeam = leagueTeamMapper.toEntity(league, team, request.season)
        val saved = leagueTeamRepository.save(leagueTeam)
        return leagueTeamMapper.toResponse(saved)
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(leagueTeamId: UUID, request: LeagueTeamUpdateRequest): LeagueTeamResponse {
        val leagueTeam = leagueTeamRepository.findById(leagueTeamId)
            .orElseThrow { LeagueTeamNotFoundException("LeagueTeam bulunamadı $leagueTeamId") }
        val team = request.teamExternalId?.let { extId ->
            teamRepository.findByExternalId(extId) ?: throw TeamNotFoundException("Takım bulunamadı: $extId")
        }
        val league = request.leagueExternalId?.let { extId ->
            val season = request.season ?: leagueTeam.season
            leagueService.findByExternalIdAndSeasonEntity(extId, season)
                ?: throw LeagueNotFoundException("Lig bulunamadı $extId, season=$season")
        }

        leagueTeam.applyManualUpdate(
            league = league,
            team = team,
            season = request.season,
        )

        return try {
            leagueTeamMapper.toResponse(leagueTeamRepository.saveAndFlush(leagueTeam))
        } catch (e: DataIntegrityViolationException) {
            throw LeagueTeamConflictException(
                "Bu güncelleme mevcut bir ilişkiyle çakışıyor (aynı league+team+season): id=$leagueTeamId"
            )
        }
    }

    // PUBLIC: season ve leagueye göre team görüntüleme
    @Transactional(readOnly = true)
    fun getLeagueTeamByLeagueIdAndSeason(leagueId: UUID, season: Int): List<LeagueTeamResponse> {
        val leagueTeams = leagueTeamRepository.findAllByLeagueIdAndSeason(leagueId, season)
        return leagueTeams.map { leagueTeamMapper.toResponse(it) }
    }

    @Transactional
    // METHOD: entity create
    fun createLeagueTeam(league: League, team: Team, season: Int): LeagueTeam =
        leagueTeamRepository.save(LeagueTeam(league = league, team = team, season = season))

    // METHOD: lig, team, seasona göre bulma
    @Transactional(readOnly = true)
    fun findByLeagueIdAndTeamIdAndSeason(leagueId: UUID, teamId: UUID, season: Int): LeagueTeam? =
        leagueTeamRepository.findByLeagueIdAndTeamIdAndSeason(leagueId, teamId, season)

    // ADMIN: elle silme
    @Transactional
    fun deleteLeagueTeam(id: UUID) {
        if (!leagueTeamRepository.existsById(id)) throw LeagueTeamNotFoundException("Takım bulunamadı: $id")
        leagueTeamRepository.deleteById(id)
    }
}