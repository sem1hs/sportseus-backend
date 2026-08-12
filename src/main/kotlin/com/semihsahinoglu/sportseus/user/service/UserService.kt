package com.semihsahinoglu.sportseus.user.service

import com.semihsahinoglu.sportseus.user.entity.User
import com.semihsahinoglu.sportseus.user.exception.UserNotFoundException
import com.semihsahinoglu.sportseus.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun getById(userId: UUID): User =
        userRepository.findById(userId).orElseThrow { UserNotFoundException("Kullanıcı bulunamadı") }

    fun getByEmail(email: String): User =
        userRepository.findByEmailIgnoreCase(email).orElseThrow { UserNotFoundException("Kullanıcı bulunamadı") }

    fun findByEmail(email: String): Optional<User> = userRepository.findByEmailIgnoreCase(email)

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmailIgnoreCase(email)

    fun save(user: User): User = userRepository.save(user)

    fun deactivate(userId: UUID) {          // soft delete
        val user = getById(userId)
        user.markDeleted()
        userRepository.save(user)
    }
}