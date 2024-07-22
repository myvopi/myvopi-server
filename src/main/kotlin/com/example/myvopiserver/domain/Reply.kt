package com.example.myvopiserver.domain

import jakarta.persistence.*

@Entity
class Reply(
    uuid: String,       // 우리쪽 UUID
    content: String,    // 내용
    modifiedCnt: Int,   // 수정 여부
    comment: Comment,   // 상위 댓글
    status: Status,     // 표시 상태
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = uuid
        protected set

    @Column(name = "content", nullable = false, updatable = true)
    var content: String = content
        protected set

    @Column(name = "modified_cnt", nullable = false, updatable = true)
    var modifiedCnt: Int = modifiedCnt
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    var status: Status = status
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
    )
    @JoinColumn(name = "comment_id", nullable = false)
    var comment: Comment = comment
        protected set

    @OneToMany(
        mappedBy = "reply",
        fetch = FetchType.LAZY,
        targetEntity = ReplyLike::class,
        cascade = [CascadeType.ALL],
    )
    var likes: MutableList<ReplyLike> = mutableListOf()
        protected set

    // TODO created by user id
    // TODO verified
}