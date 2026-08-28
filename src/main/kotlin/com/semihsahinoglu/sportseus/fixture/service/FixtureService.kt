package com.semihsahinoglu.sportseus.fixture.service

import com.semihsahinoglu.sportseus.fixture.client.FixtureApiClient
import com.semihsahinoglu.sportseus.fixture.dto.FixtureApiItem
import com.semihsahinoglu.sportseus.fixture.dto.FixtureResponse
import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.fixture.exception.FixtureMissingReferencesException
import com.semihsahinoglu.sportseus.fixture.exception.FixtureNotFoundException
import com.semihsahinoglu.sportseus.fixture.mapper.FixtureMapper
import com.semihsahinoglu.sportseus.fixture.repository.FixtureRepository
import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.service.TeamService
import com.semihsahinoglu.sportseus.venue.entity.Venue
import com.semihsahinoglu.sportseus.venue.service.VenueService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FixtureService(
    private val fixtureApiClient: FixtureApiClient,
    private val fixtureRepository: FixtureRepository,
    private val fixtureMapper: FixtureMapper,
    private val leagueService: LeagueService,
    private val teamService: TeamService,
    private val venueService: VenueService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    // ADMIN: bir takımın bir ligdeki bir sezon maçlarını sync et (toplu, ya-hep-ya-hiç)
    @Transactional
    fun sync(leagueExternalId: Int, season: Int, teamExternalId: Int): List<FixtureResponse> {
        // 1) GİRİŞ ÖN KOŞULU — league + team DB'de olmalı (yoksa hata, sync başlamaz)
        val league = leagueService.getByExternalIdAndSeasonEntity(leagueExternalId, season)
        teamService.findByExternalId(teamExternalId)   // yoksa TeamNotFoundException fırlatır

        // 2) API'den çek
        val items = fixtureApiClient.fetchByLeagueSeasonTeam(leagueExternalId, season, teamExternalId)
            .filter { it.fixture?.id != null && it.teams?.home?.id != null && it.teams?.away?.id != null }
        if (items.isEmpty()) return emptyList()

        // 3) KATI TOPLU FK — tüm league/team'leri topla, eksikse HİÇ yazmadan patlat
        val leagueMap = HashMap<Int, League>()
        val teamMap = HashMap<Int, Team>()
        val missingLeagues = LinkedHashSet<Int>()
        val missingTeams = LinkedHashSet<Int>()

        // league: response'taki her maçın ligi (genelde tek lig ama garanti için hepsi)
        items.mapNotNull { it.league?.id }.toSet().forEach { extId ->
            val found = leagueService.findByExternalIdAndSeasonEntity(extId, season)
            if (found != null) leagueMap[extId] = found else missingLeagues.add(extId)
        }
        // team: her maçın home + away
        items.flatMap { listOfNotNull(it.teams?.home?.id, it.teams?.away?.id) }.toSet().forEach { extId ->
            val found = teamService.findByExternalIdOptional(extId)
            if (found != null) teamMap[extId] = found else missingTeams.add(extId)
        }

        if (missingLeagues.isNotEmpty() || missingTeams.isNotEmpty())
            throw FixtureMissingReferencesException(missingLeagues.toList(), missingTeams.toList())

        // 4) hepsi çözüldü → tek transaction'da upsert (biri patlarsa tümü geri alınır)
        return items.map { item -> upsertOne(item, season, leagueMap, teamMap) }
    }

    // METHOD: tek fixture upsert (venue opsiyonel çözülür)
    private fun upsertOne(
        item: FixtureApiItem,
        season: Int,
        leagueMap: Map<Int, League>,
        teamMap: Map<Int, Team>,
    ): FixtureResponse {
        val league = leagueMap.getValue(item.league!!.id!!)
        val homeTeam = teamMap.getValue(item.teams!!.home!!.id!!)
        val awayTeam = teamMap.getValue(item.teams!!.away!!.id!!)

        // venue OPSİYONEL — DB'de yoksa null, maç yine kaydedilir
        val venue: Venue? = item.fixture?.venue?.id?.let { venueService.findByExternalId(it) }

        val externalId = item.fixture!!.id!!
        val existing = fixtureRepository.findByExternalId(externalId)
        val saved = if (existing != null) {
            fixtureMapper.applyApiData(existing, item, season, venue)
            fixtureRepository.save(existing)
        } else {
            fixtureRepository.save(
                fixtureMapper.toEntity(item, season, league, homeTeam, awayTeam, venue)
            )
        }
        return fixtureMapper.toResponse(saved)
    }

    // PUBLIC: bir ligin bir sezondaki tüm maçları
    @Transactional(readOnly = true)
    fun getByLeagueAndSeason(leagueExternalId: Int, season: Int): List<FixtureResponse> {
        val fixtures = fixtureRepository.findAllByLeagueExternalIdAndSeasonOrderByMatchDateAsc(leagueExternalId, season)
        return fixtures.map { fixtureMapper.toResponse(it) }
    }

    // PUBLIC: bir takımın bir sezondaki maçları (home + away)
    @Transactional(readOnly = true)
    fun getByTeamAndSeason(teamExternalId: Int, season: Int): List<FixtureResponse> =
        fixtureRepository.findAllByTeamExternalIdAndSeason(teamExternalId, season)
            .map(fixtureMapper::toResponse)

    // PUBLIC: tek maç detayı
    @Transactional(readOnly = true)
    fun getByExternalId(externalId: Long): FixtureResponse {
        val fixture = fixtureRepository.findWithRelationsByExternalId(externalId)
            ?: throw FixtureNotFoundException("Maç bulunamadı: fixture=$externalId")
        return fixtureMapper.toResponse(fixture)
    }

    // METHOD: tek maç detayı, entity
    @Transactional(readOnly = true)
    fun getByExternalIdEntity(externalId: Long): Fixture = fixtureRepository.findWithRelationsByExternalId(externalId)
        ?: throw FixtureNotFoundException("Maç bulunamadı: fixture=$externalId")

    // ADMIN: tekil silme
    @Transactional
    fun deleteById(id: UUID) {
        if (!fixtureRepository.existsById(id)) throw FixtureNotFoundException("Maç bulunamadı: id=$id")
        fixtureRepository.deleteById(id)
    }

    // ADMIN: fixture'a elle venue bağla/güncelle (venue null/eksik gelince)
    @Transactional
    fun updateVenue(fixtureExternalId: Long, venueExternalId: Int): FixtureResponse {
        val fixture = fixtureRepository.findByExternalId(fixtureExternalId)
            ?: throw FixtureNotFoundException("Maç bulunamadı: fixture=$fixtureExternalId")

        // venue DB'de olmalı — yoksa katı hata (önce venue sync et)
        val venue = venueService.getByExternalIdOrThrow(venueExternalId)

        fixture.applyVenue(venue)
        val saved = fixtureRepository.save(fixture)
        return fixtureMapper.toResponse(saved)
    }
}