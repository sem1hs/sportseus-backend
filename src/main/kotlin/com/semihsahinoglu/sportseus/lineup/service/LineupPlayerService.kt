package com.semihsahinoglu.sportseus.lineup.service

import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerAddRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerUpdateRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.entity.LineupPlayer
import com.semihsahinoglu.sportseus.lineup.exception.LineupNotFoundException
import com.semihsahinoglu.sportseus.lineup.exception.LineupPlayerConflictException
import com.semihsahinoglu.sportseus.lineup.mapper.LineupMapper
import com.semihsahinoglu.sportseus.lineup.repository.FixtureLineupRepository
import com.semihsahinoglu.sportseus.lineup.repository.LineupPlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LineupPlayerService(
    private val fixtureLineupRepository: FixtureLineupRepository,
    private val lineupPlayerRepository: LineupPlayerRepository,
    private val lineupMapper: LineupMapper,
) {
    // ADMIN: oyuncu güncelle (number/position/isStarter) + lineup manuallyEdited
    @Transactional
    fun updatePlayer(
        fixtureExternalId: Long,
        teamExternalId: Int,
        lineupPlayerId: UUID,
        request: LineupPlayerUpdateRequest,
    ): LineupResponse {
        val lineup = fixtureLineupRepository.findByFixtureExternalIdAndTeamExternalId(fixtureExternalId, teamExternalId)
            ?: throw LineupNotFoundException("Lineup bulunamadı: fixture=$fixtureExternalId team=$teamExternalId")

        val player = lineupPlayerRepository.findByIdAndLineupId(lineupPlayerId, lineup.id!!)
            ?: throw LineupNotFoundException("Oyuncu bu lineup'ta yok: player=$lineupPlayerId")

        player.updateLineupPlayer(request)    // yedeğe al / ilk 11'e çıkar
        lineup.makeManuallyEdited()           // sync artık dokunmaz

        fixtureLineupRepository.save(lineup)  // player cascade ile kaydedilir
        return lineupMapper.toResponse(lineup)
    }

    // ADMIN: lineup'a oyuncu ekle + manuallyEdited
    @Transactional
    fun addPlayer(
        fixtureExternalId: Long,
        teamExternalId: Int,
        request: LineupPlayerAddRequest,
    ): LineupResponse {
        val lineup = fixtureLineupRepository.findByFixtureExternalIdAndTeamExternalId(fixtureExternalId, teamExternalId)
            ?: throw LineupNotFoundException("Lineup bulunamadı: fixture=$fixtureExternalId team=$teamExternalId")
        // aynı oyuncu zaten var mı? (aynı playerExternalId iki kez eklenmesin)
        val alreadyExists = lineup.players.any { it.playerExternalId == request.playerExternalId }
        if (alreadyExists) throw LineupPlayerConflictException("Oyuncu zaten bu lineup'ta: player=${request.playerExternalId}")

        lineup.players.add(
            LineupPlayer(
                lineup = lineup,
                playerExternalId = request.playerExternalId,
                playerName = request.name,
                number = request.number,
                position = request.position,
                isStarter = request.isStarter,
            )
        )
        lineup.makeManuallyEdited()    // sync artık dokunmaz
        return lineupMapper.toResponse(fixtureLineupRepository.save(lineup))
    }

    // ADMIN: lineup'tan oyuncu sil + lineup manuallyEdited
    @Transactional
    fun deletePlayer(fixtureExternalId: Long, teamExternalId: Int, lineupPlayerId: UUID) {
        val lineup = fixtureLineupRepository.findByFixtureExternalIdAndTeamExternalId(fixtureExternalId, teamExternalId)
            ?: throw LineupNotFoundException("Lineup bulunamadı: fixture=$fixtureExternalId team=$teamExternalId")

        val player = lineupPlayerRepository.findByIdAndLineupId(lineupPlayerId, lineup.id!!)
            ?: throw LineupNotFoundException("Oyuncu bu lineup'ta yok: player=$lineupPlayerId")

        lineup.players.remove(player)        // orphanRemoval siler
        lineup.makeManuallyEdited()                    // sync artık dokunmaz
        fixtureLineupRepository.save(lineup)
    }
}