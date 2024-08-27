package com.example.myvopiserver.domain

import com.commoncoremodule.enums.LikeStatus
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    var status: LikeStatus = LikeStatus.LIKED
        protected set

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

    fun unlike() {
        this.status = LikeStatus.UNLIKED
    }

    fun like() {
        this.status = LikeStatus.LIKED
    }

    override fun toString(): String {
        return "CommentLike(id=$id, status=$status)"
    }

}