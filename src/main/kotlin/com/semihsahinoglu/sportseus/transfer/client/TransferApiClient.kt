package com.semihsahinoglu.sportseus.transfer.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.transfer.dto.TransferApiEnvelope
import com.semihsahinoglu.sportseus.transfer.dto.TransferApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class TransferApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /transfers?player=X → tek oyuncunun tüm transferleri
    fun fetchByPlayer(playerExternalId: Long): TransferApiItem? {
        log.info("API-Football transfer çağrısı (oyuncu): player={}", playerExternalId)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/transfers")
                    .queryParam("player", playerExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<TransferApiEnvelope>()

        return envelope?.response?.firstOrNull()
    }

    // /transfers?team=X → takımın tüm hareketleri (çok oyunculu)
    fun fetchByTeam(teamExternalId: Int): List<TransferApiItem> {
        log.info("API-Football transfer çağrısı (takım): team={}", teamExternalId)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/transfers")
                    .queryParam("team", teamExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<TransferApiEnvelope>()

        return envelope?.response ?: emptyList()
    }
}