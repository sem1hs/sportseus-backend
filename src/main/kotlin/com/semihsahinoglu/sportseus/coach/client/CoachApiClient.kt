package com.semihsahinoglu.sportseus.coach.client

import com.semihsahinoglu.sportseus.coach.dto.CoachApiEnvelope
import com.semihsahinoglu.sportseus.coach.dto.CoachApiItem
import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class CoachApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /coachs?id=X → tek antrenör (profil + career), yoksa null
    fun fetchById(coachExternalId: Int): CoachApiItem? {
        log.info("API-Football coach çağrısı (id): coach={}", coachExternalId)
        val envelope = footballRestClient.get()
            .uri { b -> b.path("/coachs").queryParam("id", coachExternalId).build() }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<CoachApiEnvelope>()
        return envelope?.response?.firstOrNull()
    }

    // /coachs?team=X → takımda görev yapmış TÜM antrenörler (çok kayıtlı)
    fun fetchByTeam(teamExternalId: Int): List<CoachApiItem> {
        log.info("API-Football coach çağrısı (team): team={}", teamExternalId)
        val envelope = footballRestClient.get()
            .uri { b -> b.path("/coachs").queryParam("team", teamExternalId).build() }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<CoachApiEnvelope>()
        return envelope?.response ?: emptyList()
    }
}