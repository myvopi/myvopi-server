package com.example.cronserver.domain.businessEntity

import jakarta.persistence.*

@Entity
@Table(name = "user")
class User {

    @Id
    var id: Long = 0L

    @Column(name = "uuid")
    var uuid: String = ""
        protected set

    @Column(name = "name")
    var name: String = ""
        protected set

    @Column(name = "dailyChance", nullable = false, updatable = true)
    var dailyChance: Int = 3
        protected set

}