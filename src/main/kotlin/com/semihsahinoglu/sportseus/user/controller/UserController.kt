package com.semihsahinoglu.sportseus.user.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.security.entity.UserPrincipal
import com.semihsahinoglu.sportseus.user.dto.UserResponse
import com.semihsahinoglu.sportseus.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun getMe(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getMe(userPrincipal.user.id)
        val response = ApiResponse.success(user)
        return ResponseEntity.ok(response)
    }
}