package com.semihsahinoglu.sportseus.security.service

import com.semihsahinoglu.sportseus.security.entity.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {
    private val signKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))
    }

    fun generateAccessToken(userDetails: UserDetails, userId: UUID): String {
        val claims = mapOf(
            "roles" to userDetails.authorities.map { it.authority },
            "userId" to userId.toString(),
        )
        return createToken(claims, userDetails.username, jwtProperties.accessTokenExpiration)
    }

    fun generateRefreshToken(email: String, userId: UUID): String {
        val claims = mapOf<String, Any>("userId" to userId.toString())
        return createToken(claims, email, jwtProperties.refreshTokenExpiration)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val claims = extractAllClaims(token)
        return claims.subject == userDetails.username && claims.expiration.after(Date())
    }

    fun extractEmail(token: String): String = extractAllClaims(token).subject

    fun extractUserId(token: String): UUID? =
        extractAllClaims(token).get("userId", String::class.java)?.let(UUID::fromString)

    fun extractJti(token: String): String = extractAllClaims(token).id

    fun refreshTokenExpiry(): Instant = Instant.now().plusMillis(jwtProperties.refreshTokenExpiration)

    fun remainingTtl(token: String): Duration {
        val exp = extractAllClaims(token).expiration.toInstant()
        val remaining = Duration.between(Instant.now(), exp)
        return if (remaining.isNegative) Duration.ZERO else remaining
    }

    private fun createToken(claims: Map<String, Any>, subject: String, ttlMillis: Long): String {
        val now = System.currentTimeMillis()
        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .claims(claims)
            .subject(subject)
            .issuedAt(Date(now))
            .expiration(Date(now + ttlMillis))
            .signWith(signKey)
            .compact()
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signKey)
            .build()
            .parseSignedClaims(token)
            .payload
}