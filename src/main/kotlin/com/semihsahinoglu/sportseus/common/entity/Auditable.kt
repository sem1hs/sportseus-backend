package com.semihsahinoglu.sportseus.common.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable {

    @Id
    @GeneratedValue
    @UuidGenerator
    var id: UUID? = null
        protected set

    @CreatedBy
    var createdBy: String? = null
        protected set

    @CreatedDate
    var createdDate: LocalDateTime? = null
        protected set

    @LastModifiedBy
    var lastModifiedBy: String? = null
        protected set

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null
        protected set
}