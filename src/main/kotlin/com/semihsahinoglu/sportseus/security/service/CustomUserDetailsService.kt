package com.semihsahinoglu.sportseus.security.service

import com.semihsahinoglu.sportseus.security.entity.UserPrincipal
import com.semihsahinoglu.sportseus.user.exception.UserNotFoundException
import com.semihsahinoglu.sportseus.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow { UserNotFoundException("$email ait kullanıcı bulunamadı") }
        return UserPrincipal(user)
    }
}