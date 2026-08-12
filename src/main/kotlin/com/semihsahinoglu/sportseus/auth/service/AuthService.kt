package com.semihsahinoglu.sportseus.auth.service

import com.semihsahinoglu.sportseus.auth.dto.JwtTokenResponse
import com.semihsahinoglu.sportseus.auth.dto.LoginRequest
import com.semihsahinoglu.sportseus.auth.dto.SignUpRequest
import com.semihsahinoglu.sportseus.auth.exception.AuthenticationFailedException
import com.semihsahinoglu.sportseus.auth.exception.InvalidRefreshTokenException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenDoesntBelongUserException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenNotFoundException
import com.semihsahinoglu.sportseus.security.service.CustomUserDetailsService
import com.semihsahinoglu.sportseus.security.service.JwtService
import com.semihsahinoglu.sportseus.security.service.TokenBlacklistService
import com.semihsahinoglu.sportseus.user.entity.Role
import com.semihsahinoglu.sportseus.user.entity.User
import com.semihsahinoglu.sportseus.user.exception.UserAlreadyExistException
import com.semihsahinoglu.sportseus.user.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService,
    private val tokenBlacklistService: TokenBlacklistService
) {

    fun login(request: LoginRequest): JwtTokenResponse {
        val email: String = request.email
        val password: String = request.password

        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
        } catch (e: AuthenticationException) {
            throw AuthenticationFailedException("E-mail veya şifre hatalı")
        }

        val userDetails = userDetailsService.loadUserByUsername(email)
        val user = userService.getByEmail(email)
        val userId = requireNotNull(user.id)

        val accessToken = jwtService.generateAccessToken(userDetails, userId)
        val refreshToken = jwtService.generateRefreshToken(email, userId)
        refreshTokenService.saveToken(user, refreshToken, jwtService.refreshTokenExpiry())

        return JwtTokenResponse(accessToken, refreshToken)
    }

    fun signUp(request: SignUpRequest): JwtTokenResponse {
        if (userService.existsByEmail(request.email))
            throw UserAlreadyExistException("Bu e-posta zaten kayıtlı!")

        val encodedPassword = requireNotNull(passwordEncoder.encode(request.password))
        val user = User(
            email = request.email,
            passwordHash = encodedPassword,
            displayName = request.displayName,
            role = Role.DEFAULT_USER,                 // varsayılan; admin/editör'ü seed veya admin paneli oluşturur
        )
        val saved = userService.save(user)
        val userId = requireNotNull(saved.id)

        val userDetails = userDetailsService.loadUserByUsername(saved.email)
        val accessToken = jwtService.generateAccessToken(userDetails, userId)
        val refreshToken = jwtService.generateRefreshToken(saved.email, userId)
        refreshTokenService.saveToken(saved, refreshToken, jwtService.refreshTokenExpiry())

        return JwtTokenResponse(accessToken, refreshToken)
    }

    fun logout(userId: UUID, accessToken: String, refreshToken: String, allDevices: Boolean) {
        // refresh token(lar)ı iptal et
        if (allDevices) refreshTokenService.revokeAllTokensForUser(userId)
        else refreshTokenService.revokeToken(userId, refreshToken)

        // access token'ı anında geçersiz kıl (kalan ömrü kadar kara listede kalır)
        val jti = jwtService.extractJti(accessToken)
        tokenBlacklistService.blacklist(jti, jwtService.remainingTtl(accessToken))
    }

    fun refreshToken(rawRefreshToken: String): JwtTokenResponse {
        // 1. JWT imza + expire (extractEmail parse ederken zaten fırlatır)
        val email = jwtService.extractEmail(rawRefreshToken)
        val userDetails = userDetailsService.loadUserByUsername(email)
        if (!jwtService.validateToken(rawRefreshToken, userDetails))
            throw InvalidRefreshTokenException("Geçersiz veya süresi dolmuş refresh token")

        val user = userService.getByEmail(email)
        val userId = requireNotNull(user.id)

        // 2. Redis'te var mı? Yoksa: ya süresi doldu ya da zaten kullanıldı/iptal edildi.
        //    Geçerli JWT + Redis'te YOK => yeniden kullanım şüphesi => tüm oturumları düşür.
        val tokenData = try {
            refreshTokenService.validateToken(rawRefreshToken)
        } catch (e: RefreshTokenNotFoundException) {
            refreshTokenService.revokeAllTokensForUser(userId)   // olası hırsızlık
            throw InvalidRefreshTokenException("Refresh token yeniden kullanıldı, tüm oturumlar kapatıldı")
        }

        // 3. Tutarlılık ağı (normalde hep geçer; geçmezse veri tutarsızlığı işareti)
        if (tokenData.userId != userId)
            throw RefreshTokenDoesntBelongUserException("Bu token kullanıcıya ait değil")

        // 4. ROTATION: eski refresh token'ı iptal et, yenisini üret ve kaydet
        refreshTokenService.revokeToken(userId, rawRefreshToken)
        val newRefreshToken = jwtService.generateRefreshToken(email, userId)
        refreshTokenService.saveToken(user, newRefreshToken, jwtService.refreshTokenExpiry())

        val newAccessToken = jwtService.generateAccessToken(userDetails, userId)
        return JwtTokenResponse(newAccessToken, newRefreshToken)   // ← artık YENİ refresh token dönüyor
    }
}