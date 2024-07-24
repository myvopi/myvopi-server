package com.example.myvopiserver.domain.role

import com.example.myvopiserver.domain.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "email_verification")
class EmailVerification(
    code: String,
    user: User,
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

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
    }
}