package com.example.myvopiserver.domain

import com.example.myvopiserver.common.config.exception.BaseException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.enums.LikeStatus
import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*

@Entity
@Table(name ="comment_like")
class CommentLike(
    comment: Comment,   // 상위 댓글
    user: User,         // 생성자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
    )
    @JoinColumn(name = "comment_id", nullable = false)
    var comment: Comment = comment
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
        if(this.status == LikeStatus.UNLIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Cannot unlike this comment")
        this.status = LikeStatus.UNLIKED
    }

    fun like() {
        if(this.status == LikeStatus.LIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Already liked")
        this.status = LikeStatus.LIKED
    }

    override fun toString(): String {
        return "CommentLike(id=$id, status=$status)"
    }

}