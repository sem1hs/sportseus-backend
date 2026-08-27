package com.semihsahinoglu.sportseus.venue.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.venue.dto.VenueApiEnvelope
import com.semihsahinoglu.sportseus.venue.dto.VenueApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class VenueApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /venues?id=X → tek venue (yoksa null)
    fun fetchById(venueExternalId: Int): VenueApiItem? {
        log.info("API-Football venue çağrısı: id={}", venueExternalId)
        val envelope = footballRestClient.get()
            .uri { b -> b.path("/venues").queryParam("id", venueExternalId).build() }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<VenueApiEnvelope>()
        return envelope?.response?.firstOrNull()
    }
}