package com.semihsahinoglu.sportseus.player.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.player.dto.PlayerNode
import com.semihsahinoglu.sportseus.player.dto.api.PlayerApiEnvelope
import com.semihsahinoglu.sportseus.player.dto.api.PlayerApiItem
import com.semihsahinoglu.sportseus.player.dto.profile.PlayerProfileApiEnvelope
import com.semihsahinoglu.sportseus.player.dto.squad.SquadApiEnvelope
import com.semihsahinoglu.sportseus.player.dto.squad.SquadApiItem
import com.semihsahinoglu.sportseus.player.dto.team.PlayerTeamsApiEnvelope
import com.semihsahinoglu.sportseus.player.dto.team.PlayerTeamsApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class PlayerApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient
) {
    private val log = LoggerFactory.getLogger(PlayerApiClient::class.java)

    // 1) /players/profiles?player=X → tek oyuncu profili (yoksa null)
    fun fetchProfile(playerExternalId: Long): PlayerNode? {
        log.info("API-Football oyuncu profili çağrısı: player={}", playerExternalId)
        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/players/profiles")
                    .queryParam("player", playerExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<PlayerProfileApiEnvelope>()

        return envelope?.response?.firstOrNull()?.player
    }

    // 2) /players?id=X&season=Y → oyuncu + statistics[] (yoksa null)
    fun fetchPlayerWithStats(playerExternalId: Long, season: Int): PlayerApiItem? {
        log.info("API-Football oyuncu+istatistik çağrısı: id={} season={}", playerExternalId, season)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/players")
                    .queryParam("id", playerExternalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(PlayerApiEnvelope::class.java)

        return envelope?.response?.firstOrNull()
    }

    // 3) /players/squads?team=X → kadro (yoksa null)
    fun fetchSquad(teamExternalId: Long): SquadApiItem? {
        log.info("API-Football kadro çağrısı: team={}", teamExternalId)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/players/squads")
                    .queryParam("team", teamExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(SquadApiEnvelope::class.java)

        return envelope?.response?.firstOrNull()
    }

    // 4) /players/teams?player=X → takım geçmişi (tüm takımlar, sezonlarıyla)
    fun fetchTeamHistory(playerExternalId: Long): List<PlayerTeamsApiItem> {
        log.info("API-Football oyuncu takım geçmişi çağrısı: player={}", playerExternalId)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/players/teams")
                    .queryParam("player", playerExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(PlayerTeamsApiEnvelope::class.java)

        return envelope?.response ?: emptyList()
    }
}