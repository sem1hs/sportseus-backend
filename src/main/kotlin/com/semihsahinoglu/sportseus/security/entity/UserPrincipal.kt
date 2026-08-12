package com.semihsahinoglu.sportseus.security.entity

import com.semihsahinoglu.sportseus.user.entity.User
import com.semihsahinoglu.sportseus.user.entity.UserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

    override fun getPassword(): String = user.passwordHash

    override fun getUsername(): String = user.email

    override fun isEnabled(): Boolean = user.status == UserStatus.ACTIVE

    override fun isAccountNonLocked(): Boolean = user.status != UserStatus.SUSPENDED

    override fun isAccountNonExpired(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true
}