package com.example.myvopiserver.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseTime {

    // TODO time calculation for region
    @CreatedDate
    @Column(name = "createdDt", updatable = false)
    var createdDt: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "updatedDt", updatable = true)
    var updatedDt: LocalDateTime? = null
}