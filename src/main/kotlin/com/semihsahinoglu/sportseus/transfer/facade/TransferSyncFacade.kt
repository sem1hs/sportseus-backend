package com.semihsahinoglu.sportseus.transfer.facade

import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.team.service.TeamService
import com.semihsahinoglu.sportseus.transfer.client.TransferApiClient
import com.semihsahinoglu.sportseus.transfer.dto.TransferApiItem
import com.semihsahinoglu.sportseus.transfer.dto.TransferResponse
import com.semihsahinoglu.sportseus.transfer.service.TransferService
import org.springframework.stereotype.Service

@Service
class TransferSyncFacade(
    private val transferApiClient: TransferApiClient,
    private val transferService: TransferService,
    private val playerService: PlayerService,
    private val teamService: TeamService
) {

    // ADMIN: oyuncuya göre sync (?player=) — player yoksa KATI hata
    fun syncByPlayer(playerExternalId: Long): List<TransferResponse> {
        val player = playerService.getByExternalIdOrThrow(playerExternalId)   // katı
        val item = transferApiClient.fetchByPlayer(playerExternalId) ?: return emptyList()
        return processItem(item, player)
    }

    // ADMIN: takıma göre sync (?team=) — çok oyunculu, player yoksa ATLA+logla
    fun syncByTeam(teamExternalId: Int): List<TransferResponse> {
        val items = transferApiClient.fetchByTeam(teamExternalId)

        return items.flatMap { item ->
            val extId = item.player?.id ?: return@flatMap emptyList()

            val player = try {
                playerService.getByExternalIdOrThrow(extId)   // yoksa atla+logla (stub YOK)
            } catch (e: Exception) {
                return@flatMap emptyList()
            }
            processItem(item, player)
        }
    }

    // METHOD: bir player item'ındaki transfers[] listesini işle (iki mod ortak)
    private fun processItem(item: TransferApiItem, player: Player): List<TransferResponse> =
        item.transfers.mapNotNull { node ->
            val inId = node.teams?.`in`?.id
            val outId = node.teams?.out?.id
            val date = node.date

            // null in/out/date → çöp, atla
            if (inId == null || outId == null || date == null) return@mapNotNull null

            // team çöz — DB'de yoksa ESNEK atla
            val teamIn = teamService.findByExternalIdOptional(inId.toInt())
            val teamOut = teamService.findByExternalIdOptional(outId.toInt())

            if (teamIn == null || teamOut == null) return@mapNotNull null

            // per-transfer tx (upsertOne @Transactional, facade değil)
            try {
                transferService.upsertOne(player, teamIn, teamOut, date, node.type ?: "")
            } catch (e: Exception) {
                null
            }
        }
}