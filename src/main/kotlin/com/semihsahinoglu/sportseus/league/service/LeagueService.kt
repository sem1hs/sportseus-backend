package com.semihsahinoglu.sportseus.league.service

import com.semihsahinoglu.sportseus.league.client.LeagueApiClient
import com.semihsahinoglu.sportseus.league.dto.LeagueResponse
import com.semihsahinoglu.sportseus.league.dto.LeagueUpdateRequest
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.exception.LeagueAlreadyExistsException
import com.semihsahinoglu.sportseus.league.exception.LeagueNotFoundException
import com.semihsahinoglu.sportseus.league.exception.LeagueNotFoundInApiException
import com.semihsahinoglu.sportseus.league.mapper.LeagueMapper
import com.semihsahinoglu.sportseus.league.repository.LeagueRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LeagueService(
    val leagueRepository: LeagueRepository,
    val leagueApiClient: LeagueApiClient,
    val leagueMapper: LeagueMapper
) {

    // PUBLIC: tek lig (sadece DB)
    @Transactional(readOnly = true)
    fun getByExternalIdAndSeason(externalId: Int, season: Int): LeagueResponse {
        val league = leagueRepository.findByExternalIdAndSeason(externalId, season)
            ?: throw LeagueNotFoundException("Lig bulunamadı: $externalId ($season)")
        return leagueMapper.toResponse(league)
    }

    // METHOD: tek lig, entity dönen
    fun getByExternalIdAndSeasonEntity(externalId: Int, season: Int): League =
        leagueRepository.findByExternalIdAndSeason(externalId, season)
            ?: throw LeagueNotFoundException("Lig bulunamadı: $externalId ($season)")

    // METHOD: referans dönen
    fun getReferenceById(leagueId: UUID): League = leagueRepository.getReferenceById(leagueId)

    // PUBLIC: sezondaki tüm ligler (sadece DB)
    @Transactional(readOnly = true)
    fun getAllBySeason(season: Int): List<LeagueResponse> {
        val leagues = leagueRepository.findBySeason(season)
        return leagues.map(leagueMapper::toResponse)
    }

    // ADMIN: API-Football'dan çekip kaydet (Create)
    @Transactional
    fun importLeague(externalId: Int, season: Int): LeagueResponse {
        // Zaten varsa tekrar import etme — refresh kullanılmalı
        if (leagueRepository.findByExternalIdAndSeason(externalId, season) != null)
            throw LeagueAlreadyExistsException("Lig zaten kayıtlı: $externalId ($season). Tazelemek için refresh kullanın.")

        val item = leagueApiClient.fetchLeague(externalId, season)
            ?: throw LeagueNotFoundInApiException("API-Football'da lig bulunamadı: $externalId ($season)")

        val league = leagueMapper.toEntity(item, season)
        val saved = leagueRepository.save(league)
        return leagueMapper.toResponse(saved)
    }

    // ADMIN: elle partial güncelleme (Update - manuel)
    @Transactional
    fun updateLeague(id: UUID, request: LeagueUpdateRequest): LeagueResponse {
        val league = leagueRepository.findById(id).orElseThrow { LeagueNotFoundException("Lig bulunamadı: $id") }
        league.updateEntity(request)   // entity içindeki partial update
        val saved = leagueRepository.save(league)
        return leagueMapper.toResponse(saved)
    }

    // ADMIN: hard delete
    @Transactional
    fun deleteLeague(id: UUID) {
        if (!leagueRepository.existsById(id)) throw LeagueNotFoundException("Lig bulunamadı: $id")
        leagueRepository.deleteById(id)
    }
}