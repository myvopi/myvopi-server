package com.example.myvopiserver.domain

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name ="reply")
class Reply(
    content: String,            // 내용
    modifiedCnt: Int,           // 수정 여부
    comment: Comment,           // 상위 댓글
    status: CommentStatus,      // 표시 상태
    user: User,                 // 생성자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = UUID.randomUUID().toString()
        protected set

    @Column(name = "content", nullable = false, updatable = true, columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Column(name = "modified_cnt", nullable = false, updatable = true)
    var modifiedCnt: Int = modifiedCnt
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status", nullable = false, updatable = true)
    var status: CommentStatus = status
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

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        protected set

    override fun toString(): String {
        return "Reply(id=$id, uuid='$uuid', content='$content', modifiedCnt=$modifiedCnt, status=$status, comment=$comment, likes=$likes, user=$user)"
    }
    // TODO verified

}