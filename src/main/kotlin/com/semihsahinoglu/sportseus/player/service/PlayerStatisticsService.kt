package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatItem
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatisticsNode
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.exception.PlayerStatisticsNotFoundException
import com.semihsahinoglu.sportseus.player.mapper.PlayerStatisticsMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerStatisticsRepository
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@Service
class PlayerStatisticsService(
    private val playerStatisticsRepository: PlayerStatisticsRepository,
    private val playerStatisticsMapper: PlayerStatisticsMapper,
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

        val saved = if (existing != null) {
            existing.stats = json
            playerStatisticsRepository.save(existing)
        } else {
            playerStatisticsRepository.save(playerStatisticsMapper.toEntity(player, team, league, season, json))
        }

        // node zaten elimizde → tekrar deserialize etmeden response kur
        return playerStatisticsMapper.toResponse(saved, node)
    }

    // PUBLIC: oyuncunun bir sezondaki tüm lig istatistikleri
    @Transactional(readOnly = true)
    fun getByPlayerAndSeason(playerExternalId: Long, season: Int): List<PlayerStatisticsResponse> =
        playerStatisticsRepository
            .findAllByPlayerExternalIdAndSeason(playerExternalId, season)
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