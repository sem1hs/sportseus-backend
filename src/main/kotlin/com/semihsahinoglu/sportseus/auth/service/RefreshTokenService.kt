package com.semihsahinoglu.sportseus.auth.service

import com.semihsahinoglu.sportseus.auth.dto.RefreshTokenData
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenDoesntBelongUserException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenNotFoundException
import com.semihsahinoglu.sportseus.user.entity.User
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class RefreshTokenService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val tokenHashService: TokenHashService
) {
    companion object {
        private const val TOKEN_PREFIX = "refresh_token:"
        private const val USER_TOKENS_PREFIX = "user_tokens:"
    }

    fun saveToken(user: User, rawToken: String, expiresAt: Instant) {
        val tokenHash = tokenHashService.hash(rawToken)
        val tokenKey = TOKEN_PREFIX + tokenHash
        val userSetKey = USER_TOKENS_PREFIX + user.id

        val ttl = Duration.between(Instant.now(), expiresAt)
        require(!ttl.isNegative && !ttl.isZero) { "expiresAt geçmişte olamaz" }

        val now = Instant.now()
        val data = RefreshTokenData(user.id!!, tokenHash, now, now, expiresAt)

        redisTemplate.opsForValue().set(tokenKey, data, ttl)
        redisTemplate.opsForSet().add(userSetKey, tokenHash)
        redisTemplate.expire(userSetKey, ttl)
    }

    fun revokeToken(userId: UUID, rawToken: String) {
        val tokenHash = tokenHashService.hash(rawToken)
        val tokenKey = TOKEN_PREFIX + tokenHash

        val data = redisTemplate.opsForValue().get(tokenKey) as? RefreshTokenData
            ?: throw RefreshTokenNotFoundException("Token bulunamadı")
        if (data.userId != userId)
            throw RefreshTokenDoesntBelongUserException("Bu token kullanıcıya ait değil")

        redisTemplate.delete(tokenKey)
        redisTemplate.opsForSet().remove(USER_TOKENS_PREFIX + userId, tokenHash)
    }

    fun validateToken(rawToken: String): RefreshTokenData {
        val tokenHash = tokenHashService.hash(rawToken)
        val tokenKey = TOKEN_PREFIX + tokenHash

        val data = redisTemplate.opsForValue().get(tokenKey) as? RefreshTokenData
            ?: throw RefreshTokenNotFoundException("Token geçersiz veya süresi dolmuş")

        // lastUsedAt güncelle, mevcut TTL'i koru
        val remainingTtl = redisTemplate.getExpire(tokenKey, TimeUnit.SECONDS)
        val updated = data.copy(lastUsedAt = Instant.now())
        if (remainingTtl != null && remainingTtl > 0)
            redisTemplate.opsForValue().set(tokenKey, updated, Duration.ofSeconds(remainingTtl))

        return updated
    }

    fun revokeAllTokensForUser(userId: UUID) {
        val userSetKey = USER_TOKENS_PREFIX + userId
        redisTemplate.opsForSet().members(userSetKey)?.forEach { hash -> redisTemplate.delete(TOKEN_PREFIX + hash) }
        redisTemplate.delete(userSetKey)
    }
}