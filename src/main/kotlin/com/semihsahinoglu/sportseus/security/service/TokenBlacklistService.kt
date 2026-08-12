package com.semihsahinoglu.sportseus.security.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TokenBlacklistService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    companion object {
        private const val BLACKLIST_PREFIX = "blacklist:access:"
    }

    // jti'yi token'ın kalan ömrü kadar kara listeye al.
    // TTL sayesinde token zaten expire olunca anahtar kendiliğinden silinir — çöp birikmez.
    fun blacklist(jti: String, ttl: Duration) {
        if (ttl.isZero || ttl.isNegative) return   // zaten ölü token, yazmaya gerek yok
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jti, "revoked", ttl)
    }

    fun isBlacklisted(jti: String): Boolean =
        redisTemplate.hasKey(BLACKLIST_PREFIX + jti)
}