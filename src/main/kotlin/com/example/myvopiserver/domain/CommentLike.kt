package com.example.myvopiserver.domain

import jakarta.persistence.*

@Entity
class CommentLike(
    comment: Comment,   // 상위 댓글
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

    // TODO created by user id
}