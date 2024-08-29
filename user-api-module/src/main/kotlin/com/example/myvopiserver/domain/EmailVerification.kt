package com.example.myvopiserver.domain

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "email_verification")
@DynamicUpdate
@DynamicInsert
class EmailVerification(
    code: String,
    user: User,
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "chance", nullable = false, updatable = true)
    var chance: Int = 3
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        protected set

    @Column(name = "code", nullable = false, updatable = true)
    var code: String = code
        protected set

    fun setNewCode(code: String) {
        this.code = code
        this.chance = 3
    }

    fun removeChance() {
        this.chance -= 1
    }

    override fun toString(): String {
        return "EmailVerification(id=$id, code='$code', chance=$chance)"
    }
}