package com.semihsahinoglu.sportseus.team.service

import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.team.client.TeamApiClient
import com.semihsahinoglu.sportseus.team.dto.TeamApiItem
import com.semihsahinoglu.sportseus.team.dto.TeamResponse
import com.semihsahinoglu.sportseus.team.dto.TeamUpdateRequest
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.exception.TeamHasFixturesException
import com.semihsahinoglu.sportseus.venue.entity.Venue
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.mapper.TeamMapper
import com.semihsahinoglu.sportseus.team.repository.TeamRepository
import com.semihsahinoglu.sportseus.venue.service.VenueService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TeamService(
    private val teamApiClient: TeamApiClient,
    private val teamRepository: TeamRepository,
    private val venueService: VenueService,
    private val leagueTeamService: LeagueTeamService,
    private val leagueService: LeagueService,
    private val teamMapper: TeamMapper,
) {
    // ADMIN: ligin tüm takımlarını senkronla
    @Transactional
    fun syncTeamsByLeague(leagueExternalId: Int, season: Int): List<TeamResponse> {
        // 1. KATI ÖN KOŞUL: lig import edilmiş olmalı
        val league = leagueService.getByExternalIdAndSeasonEntity(leagueExternalId, season)
        val items = teamApiClient.fetchTeamsByLeague(leagueExternalId, season)
        return items.map { item -> upsertTeamWithLeague(item, league.id!!, season) }
    }

    // ADMIN: tek takım senkronla
    @Transactional
    fun syncSingleTeam(teamExternalId: Int, leagueExternalId: Int, season: Int): TeamResponse {
        val league = leagueService.getByExternalIdAndSeasonEntity(leagueExternalId, season)
        val item = teamApiClient.fetchTeam(teamExternalId, leagueExternalId, season)
            ?: throw TeamNotFoundException("API-Football'da takım bulunamadı: $teamExternalId (lig $leagueExternalId, sezon $season)")

        return upsertTeamWithLeague(item, league.id!!, season)
    }

    // ADMIN: hard delete
    @Transactional
    fun deleteTeam(id: UUID) {
        if (!teamRepository.existsById(id)) throw TeamNotFoundException("Takım bulunamadı: $id")
        try {
            teamRepository.deleteById(id)
            teamRepository.flush()
        } catch (e: DataIntegrityViolationException) {
            throw TeamHasFixturesException("Bu takıma bağlı kayıtlar (maç, puan durumu vb.) var; önce onları silmelisin")
        }
    }

    // PUBLIC: tek takım (venue gömülü)
    @Transactional(readOnly = true)
    fun getByExternalId(externalId: Int): TeamResponse {
        val team = teamRepository.findByExternalIdWithVenue(externalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: $externalId")
        return teamMapper.toResponse(team)
    }

    // METHOD: externalId'den find
    fun findByExternalId(externalId: Int): Team =
        teamRepository.findByExternalId(externalId) ?: throw TeamNotFoundException("Takım bulunamadı: $externalId")

    // METHOD: externalId'den find optional
    fun findByExternalIdOptional(externalId: Int): Team? =
        teamRepository.findByExternalId(externalId)

    // Ortak upsert: venue → team → league_teams (sırayla)
    private fun upsertTeamWithLeague(item: TeamApiItem, leagueId: UUID, season: Int): TeamResponse {

        // a. VENUE upsert (venue.id null ise venue'siz devam)
        val venue: Venue? = venueService.upsertVenue(item)

        // b. TEAM upsert
        val existingTeam = teamRepository.findByExternalId(item.team.id)
        val team = if (existingTeam != null) {
            if (existingTeam.manuallyEdited) existingTeam
            else {
                teamMapper.applyApiData(existingTeam, item.team, venue)
                teamRepository.save(existingTeam)
            }
        } else {
            teamRepository.save(teamMapper.toEntity(item.team, venue))
        }

        // c. LEAGUE_TEAMS ilişkisi (yoksa ekle — duplicate önleme)
        val existingRelation = leagueTeamService.findByLeagueIdAndTeamIdAndSeason(leagueId, team.id!!, season)
        if (existingRelation == null) {
            val league = leagueService.getReferenceById(leagueId)   // proxy, ekstra sorgu yok
            leagueTeamService.createLeagueTeam(league, team, season)
        }

        return teamMapper.toResponse(team)
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(teamExternalId: Int, request: TeamUpdateRequest): TeamResponse {
        val team = teamRepository.findByExternalId(teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: $teamExternalId")

        team.applyManualUpdate(
            name = request.name,
            code = request.code,
            country = request.country,
            founded = request.founded,
            national = request.national,
            logoUrl = request.logoUrl,
        )

        return teamMapper.toResponse(teamRepository.save(team))
    }

    // ADMIN: takıma elle venue bağla/güncelle (fixture'da venue null gelince)
    @Transactional
    fun updateTeamVenue(teamExternalId: Int, venueExternalId: Int): TeamResponse {
        val team = teamRepository.findByExternalId(teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: $teamExternalId")

        // venue DB'de olmalı — yoksa katı hata (önce venue sync et)
        val venue = venueService.getByExternalIdOrThrow(venueExternalId)

        team.applyVenue(venue)
        val saved = teamRepository.save(team)

        return teamMapper.toResponse(saved)
    }
}