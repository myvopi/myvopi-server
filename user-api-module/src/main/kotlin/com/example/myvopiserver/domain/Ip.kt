package com.example.myvopiserver.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "ip")
class Ip {

    @Id
    var id = ""

    @Column(name = "host", nullable = false, updatable = false)
    var host = ""
        protected set

    @Column(name = "port", nullable = false, updatable = false)
    var port = 0
        protected set

    @Column(name = "url", nullable = false, updatable = false)
    var url = ""
        protected set

    @Column(name = "createdDt", nullable = false, updatable = false)
    var createdDt: LocalDateTime? = null
}