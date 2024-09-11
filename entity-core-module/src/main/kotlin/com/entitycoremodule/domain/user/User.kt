package com.entitycoremodule.domain.user

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.enums.RoleStatus
import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.domain.BaseTime
import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.comment.Comment
import com.entitycoremodule.domain.reply.Reply
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
class User(
    name: String,               // 성명
    userId: String,             // 아이디
    nationality: CountryCode,   // 국적
    password: String,           // 비번
    email: String,              // 이메일
): BaseTime() {

    constructor(
        command: InternalUserCommand,
    ) : this(
        command.name,
        command.userId,
        command.nationality,
        command.password,
        command.email
    )
    {
        this.id = command.id
        this.uuid = command.uuid
        this.role = command.role
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = UUID.randomUUID().toString()
        protected set

    @Column(name = "name", nullable = false, updatable = true)
    var name: String = name
        protected set

    @Column(name = "user_id", nullable = false, updatable = true)
    var userId: String = userId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false, updatable = true)
    var nationality: CountryCode = nationality
        protected set

    @Column(name = "password", nullable = false, updatable = true)
    var password: String = password
        protected set

    @Column(name = "email", nullable = false, updatable = true)
    var email: String = email
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    var status: RoleStatus = RoleStatus.ACTIVE
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = true)
    var role: MemberRole = MemberRole.ROLE_UNVERIFIED
        protected set

    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
        cascade = [CascadeType.ALL],
    )
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        targetEntity = Reply::class,
        cascade = [CascadeType.ALL],
    )
    var replies: MutableList<Reply> = mutableListOf()
        protected set

    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        targetEntity = EmailVerification::class,
        cascade = [CascadeType.ALL],
    )
    var verifications: MutableList<EmailVerification> = mutableListOf()
        protected set

    fun setRoleStatusBanned() {
        this.status = RoleStatus.BANNED
    }

    fun setMemberRoleUser() {
        this.role = MemberRole.ROLE_USER
    }

    override fun toString(): String {
        return "User(id=$id, uuid='$uuid', name='$name', userId='$userId', nationality=$nationality, password='$password', email='$email', status=$status, role=$role)"
    }
}