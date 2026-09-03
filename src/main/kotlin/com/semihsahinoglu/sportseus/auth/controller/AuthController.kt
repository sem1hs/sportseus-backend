package com.semihsahinoglu.sportseus.auth.controller

import com.semihsahinoglu.sportseus.auth.dto.AuthResponse
import com.semihsahinoglu.sportseus.auth.dto.LoginRequest
import com.semihsahinoglu.sportseus.auth.dto.SignUpRequest
import com.semihsahinoglu.sportseus.auth.service.AuthService
import com.semihsahinoglu.sportseus.auth.service.RefreshTokenCookieFactory
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.security.entity.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieFactory: RefreshTokenCookieFactory
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val tokenResponse = authService.login(request)
        val cookie = cookieFactory.create(tokenResponse.refreshToken).toString()
        val authResponse = AuthResponse(tokenResponse.accessToken)
        val response = ApiResponse.success(authResponse)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(response)
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val tokenResponse = authService.signUp(request)
        val cookie = cookieFactory.create(tokenResponse.refreshToken).toString()
        val authResponse = AuthResponse(tokenResponse.accessToken)
        val response = ApiResponse.success(authResponse)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(response)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@CookieValue("refreshToken") refreshToken: String): ResponseEntity<ApiResponse<AuthResponse>> {
        val tokenResponse = authService.refreshToken(refreshToken)
        val cookie = cookieFactory.create(tokenResponse.refreshToken).toString()
        val authResponse = AuthResponse(tokenResponse.accessToken)
        val response = ApiResponse.success(authResponse)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(response)
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestHeader("Authorization") authHeader: String,
        @CookieValue("refreshToken") refreshToken: String,
    ): ResponseEntity<Void> {
        val userId = requireNotNull(userPrincipal.user.id)
        val accessToken = authHeader.removePrefix("Bearer ").trim()
        val cookie = cookieFactory.clear().toString()
        authService.logout(userId, accessToken, refreshToken, allDevices = false)
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build()
    }
}