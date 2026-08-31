package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsUpdateRequest
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatItem
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatisticsNode
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.entity.PlayerStatistics
import com.semihsahinoglu.sportseus.player.exception.PlayerStatisticsConflictException
import com.semihsahinoglu.sportseus.player.exception.PlayerStatisticsNotFoundException
import com.semihsahinoglu.sportseus.player.mapper.PlayerStatisticsMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerStatisticsRepository
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@Service
class PlayerStatisticsService(
    private val playerStatisticsRepository: PlayerStatisticsRepository,
    private val playerStatisticsMapper: PlayerStatisticsMapper,
    private val playerService: PlayerService,
    private val teamService: TeamService,
    private val leagueService: LeagueService,
    private val jsonMapper: JsonMapper,
) {
    // ADMIN: upsert metodu
    @Transactional
    fun upsert(
        player: Player,
        team: Team,
        league: League,
        season: Int,
        item: PlayerStatItem,
    ): PlayerStatisticsResponse {
        val node = playerStatisticsMapper.toNode(item)
        val json = jsonMapper.writeValueAsString(node)   // DTO → jsonb String

        val existing = playerStatisticsRepository
            .findByPlayerIdAndTeamIdAndLeagueIdAndSeason(player.id!!, team.id!!, league.id!!, season)

        val saved = when {
            existing == null -> playerStatisticsRepository.save(
                PlayerStatistics(player = player, team = team, league = league, season = season, stats = json)
            )

            existing.manualAdded || existing.manuallyEdited -> existing   // elle → dokunma
            else -> {
                existing.stats = json
                playerStatisticsRepository.save(existing)
            }
        }

        // node zaten elimizde → tekrar deserialize etmeden response kur
        return playerStatisticsMapper.toResponse(saved, node)
    }

    // ADMIN: elle istatistik ekle (playerId UUID + team/league external, çakışma 409)
    @Transactional
    fun create(request: PlayerStatisticsCreateRequest): PlayerStatisticsResponse {
        // player UUID ile (elle-player de bulunur)
        val player = playerService.findById(request.playerId)

        // team + league external id ile (katı)
        val team = teamService.findByExternalIdOptional(request.teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: team=${request.teamExternalId}")
        val league = leagueService.getByExternalIdAndSeasonEntity(request.leagueExternalId, request.season)

        // composite çakışma → 409
        val exists = playerStatisticsRepository.findByPlayerIdAndTeamIdAndLeagueIdAndSeason(
            player.id!!,
            team.id!!,
            league.id!!,
            request.season
        ) != null
        if (exists) throw PlayerStatisticsConflictException("Bu istatistik zaten var: player=${request.playerId} team=${request.teamExternalId} league=${request.leagueExternalId} season=${request.season}")

        // node kur → jsonb serialize (sync ile aynı yapı)
        val node = PlayerStatisticsNode(games = request.games, goals = request.goals)
        val json = jsonMapper.writeValueAsString(node)

        val playerStatistics = playerStatisticsMapper.toManuelEntity(player, team, league, request.season, json)
        val saved = playerStatisticsRepository.save(playerStatistics)
        return playerStatisticsMapper.toResponse(saved, node)
    }

    // ADMIN: istatistik elle güncelle (games/goals) + manuallyEdited
    @Transactional
    fun update(id: UUID, request: PlayerStatisticsUpdateRequest): PlayerStatisticsResponse {
        val stat = playerStatisticsRepository.findById(id)
            .orElseThrow { PlayerStatisticsNotFoundException("İstatistik bulunamadı: id=$id") }

        // FK değişimleri — verilmişse çöz (katı)
        request.playerId?.let { pid ->
            stat.player = playerService.findById(pid)
        }
        request.teamExternalId?.let { extId ->
            stat.team = teamService.findByExternalIdOptional(extId)
                ?: throw TeamNotFoundException("Takım bulunamadı: team=$extId")
        }
        request.leagueExternalId?.let { extId ->
            val season = request.season ?: stat.season
            stat.league = leagueService.getByExternalIdAndSeasonEntity(extId, season)
        }
        request.season?.let { stat.season = it }

        // jsonb — partial blok merge
        if (request.games != null || request.goals != null) {
            val existingNode = jsonMapper.readValue(stat.stats, PlayerStatisticsNode::class.java)
            val mergedNode = PlayerStatisticsNode(
                games = request.games ?: existingNode.games,
                goals = request.goals ?: existingNode.goals,
            )
            stat.stats = jsonMapper.writeValueAsString(mergedNode)
        }

        stat.applyManuallyEdited()

        // composite key değiştiyse çakışma kontrolü (saveAndFlush + catch)
        return try {
            val saved = playerStatisticsRepository.saveAndFlush(stat)
            val node = jsonMapper.readValue(saved.stats, PlayerStatisticsNode::class.java)
            playerStatisticsMapper.toResponse(saved, node)
        } catch (e: DataIntegrityViolationException) {
            throw PlayerStatisticsConflictException(
                "Bu güncelleme mevcut bir istatistikle çakışıyor (aynı player+team+league+season): id=$id"
            )
        }
    }

    // PUBLIC: oyuncunun bir sezondaki tüm lig istatistikleri
    @Transactional(readOnly = true)
    fun getByPlayerAndSeason(playerId: UUID, season: Int): List<PlayerStatisticsResponse> =
        playerStatisticsRepository
            .findAllByPlayerIdAndSeason(playerId, season)
            .map { entity ->
                val node = jsonMapper.readValue(entity.stats, PlayerStatisticsNode::class.java)
                playerStatisticsMapper.toResponse(entity, node)
            }

    // ADMIN: delete, id ile (team modülüyle aynı)
    @Transactional
    fun deleteById(id: UUID): Unit {
        if (!playerStatisticsRepository.existsById(id))
            throw PlayerStatisticsNotFoundException("İstatistik bulunamadı: id=$id")
        playerStatisticsRepository.deleteById(id)
    }
}