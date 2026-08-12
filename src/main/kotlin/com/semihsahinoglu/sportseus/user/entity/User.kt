package com.semihsahinoglu.sportseus.user.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "users", schema = "auth")
class User(

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "display_name", nullable = false, length = 100)
    var displayName: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var role: Role,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "avatar_url", length = 500)
    var avatarUrl: String? = null,
) : Auditable() {

    fun suspend() {
        status = UserStatus.SUSPENDED
    }

    fun activate() {
        status = UserStatus.ACTIVE
    }

    fun markDeleted() {
        status = UserStatus.DELETED
    }
}