package com.semihsahinoglu.sportseus.venue.service

import com.semihsahinoglu.sportseus.team.dto.TeamApiItem
import com.semihsahinoglu.sportseus.venue.client.VenueApiClient
import com.semihsahinoglu.sportseus.venue.dto.VenueResponse
import com.semihsahinoglu.sportseus.venue.dto.VenueUpdateRequest
import com.semihsahinoglu.sportseus.venue.entity.Venue
import com.semihsahinoglu.sportseus.venue.exception.VenueNotFoundException
import com.semihsahinoglu.sportseus.venue.mapper.VenueMapper
import com.semihsahinoglu.sportseus.venue.repository.VenueRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VenueService(
    private val venueApiClient: VenueApiClient,
    private val venueRepository: VenueRepository,
    private val venueMapper: VenueMapper,
) {
    // ADMIN: API'den venue çek + upsert (/venues?id=)
    @Transactional
    fun syncById(venueExternalId: Int): VenueResponse {
        val item = venueApiClient.fetchById(venueExternalId)
            ?: throw VenueNotFoundException("API-Football'da venue bulunamadı: id=$venueExternalId")

        val existing = venueRepository.findByExternalId(item.id!!)
        val saved = when {
            existing == null -> venueRepository.save(venueMapper.toEntity(item))
            existing.manuallyEdited -> existing
            else -> {
                venueMapper.applyApiData(existing, item)
                venueRepository.save(existing)
            }
        }
        return venueMapper.toResponse(saved)
    }

    // METHOD: VENUE upsert (venue.id null ise venue'siz devam)
    fun upsertVenue(item: TeamApiItem): Venue? {
        return item.venue?.let { venueNode ->
            venueMapper.toEntity(venueNode)?.let { mapped ->
                val existing = venueRepository.findByExternalId(mapped.externalId)
                if (existing != null) {
                    venueMapper.applyApiData(existing, venueNode)
                    venueRepository.save(existing)
                } else {
                    venueRepository.save(mapped)
                }
            }
        }
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(venueExternalId: Int, request: VenueUpdateRequest): VenueResponse {
        val venue = venueRepository.findByExternalId(venueExternalId)
            ?: throw VenueNotFoundException("Venue bulunamadı: id=$venueExternalId")

        venue.applyManualUpdate(
            name = request.name,
            address = request.address,
            city = request.city,
            country = request.country,
            capacity = request.capacity,
            surface = request.surface,
            imageUrl = request.imageUrl,
        )
        return venueMapper.toResponse(venueRepository.save(venue))
    }

    // METHOD: id ile venue bulma
    fun findByExternalId(externalId: Int): Venue? =
        venueRepository.findByExternalId(externalId)

    // METHOD: externalId'den entity (Team modülü venue bağlarken kullanır) — katı
    @Transactional(readOnly = true)
    fun getByExternalIdOrThrow(venueExternalId: Int): Venue =
        venueRepository.findByExternalId(venueExternalId)
            ?: throw VenueNotFoundException("Venue bulunamadı: id=$venueExternalId. Önce venue sync edin.")

    // PUBLIC: tek venue
    @Transactional(readOnly = true)
    fun getByExternalId(externalId: Int): VenueResponse = venueMapper.toResponse(getByExternalIdOrThrow(externalId))

}