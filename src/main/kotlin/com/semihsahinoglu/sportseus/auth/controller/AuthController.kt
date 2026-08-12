package com.semihsahinoglu.sportseus.auth.controller

import com.semihsahinoglu.sportseus.auth.dto.JwtTokenResponse
import com.semihsahinoglu.sportseus.auth.dto.LoginRequest
import com.semihsahinoglu.sportseus.auth.dto.LogoutRequest
import com.semihsahinoglu.sportseus.auth.dto.RefreshTokenRequest
import com.semihsahinoglu.sportseus.auth.dto.SignUpRequest
import com.semihsahinoglu.sportseus.auth.service.AuthService
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.security.entity.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<JwtTokenResponse>> {
        val tokenResponse = authService.login(request)
        val response = ApiResponse.success(tokenResponse)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpRequest): ResponseEntity<ApiResponse<JwtTokenResponse>> {
        val tokenResponse = authService.signUp(request)
        val response = ApiResponse.success(tokenResponse)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody logoutRequest: LogoutRequest,
    ): ResponseEntity<Void> {
        val userId = requireNotNull(userPrincipal.user.id)
        val accessToken = authHeader.removePrefix("Bearer ").trim()
        authService.logout(userId, accessToken, logoutRequest.refreshToken, logoutRequest.allDevices)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResponse<JwtTokenResponse>> {
        val tokenResponse = authService.refreshToken(request.refreshToken)
        val response = ApiResponse.success(tokenResponse)
        return ResponseEntity.ok(response)
    }
}