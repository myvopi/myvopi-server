package com.example.myvopiserver.domain

import jakarta.persistence.*

@Entity
class ReplyLike(
    reply: Reply,   // 상위 댓글
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

    // TODO created by user id
}