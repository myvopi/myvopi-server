package com.example.myvopiserver.domain

import com.example.myvopiserver.common.config.exception.BaseException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.enums.LikeStatus
import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*

@Entity
@Table(name ="reply_like")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "like_status", nullable = false, updatable = true)
    var status: LikeStatus = LikeStatus.LIKED
        protected set

    fun unlike() {
        if(this.status == LikeStatus.UNLIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Cannot unlike this reply")
        this.status = LikeStatus.UNLIKED
    }

    fun like() {
        if(this.status == LikeStatus.LIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Already liked")
        this.status = LikeStatus.LIKED
    }

    override fun toString(): String {
        return "ReplyLike(id=$id, status=$status)"
    }
}