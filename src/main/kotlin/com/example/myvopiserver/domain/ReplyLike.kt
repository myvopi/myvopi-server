package com.example.myvopiserver.domain

import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*

@Entity
class ReplyLike(
    reply: Reply,   // 상위 댓글
    user: User,     // 생성자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Reply::class,
    )
    @JoinColumn(name = "reply_id", nullable = false)
    var reply: Reply = reply
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        protected set
}