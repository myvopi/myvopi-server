package com.example.myvopiserver.domain

import com.commoncoremodule.enums.LikeStatus
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name ="reply_like")
@DynamicUpdate
@DynamicInsert
class ReplyLike(
    reply: Reply,   // 상위 댓글
    user: User,     // 생성자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    var status: LikeStatus = LikeStatus.LIKED
        protected set

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

    fun unlike() {
        this.status = LikeStatus.UNLIKED
    }

    fun like() {
        this.status = LikeStatus.LIKED
    }

    override fun toString(): String {
        return "ReplyLike(id=$id, status=$status)"
    }
}