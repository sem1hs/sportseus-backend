package com.semihsahinoglu.sportseus.security.filter

import com.semihsahinoglu.sportseus.security.service.JwtService
import com.semihsahinoglu.sportseus.security.service.TokenBlacklistService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val tokenBlacklistService: TokenBlacklistService,

    @Qualifier("handlerExceptionResolver")
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authHeader = request.getHeader("Authorization")

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                val email = jwtService.extractEmail(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val jti = jwtService.extractJti(token)

                    // Kara listedeyse (logout edilmiş) auth kurma — token anında geçersiz
                    if (tokenBlacklistService.isBlacklisted(jti)) {
                        filterChain.doFilter(request, response)
                        return
                    }

                    val userDetails = userDetailsService.loadUserByUsername(email)
                    log.debug("User yüklendi: {}", userDetails.username)

                    if (jwtService.validateToken(token, userDetails) && userDetails.isEnabled) {
                        val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }
            }

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, ex)
        }
    }
}