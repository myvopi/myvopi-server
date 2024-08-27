package com.example.myvopiserver.domain

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.VerifyStatus
import com.example.myvopiserver.domain.command.InternalReplyCommand
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name ="reply")
class Reply(
    content: String,            // 내용
    comment: Comment,           // 상위 댓글
    user: User,                 // 생성자
): BaseTime() {

    constructor(
        command: InternalReplyCommand,
        comment: Comment,
        user: User,
    ) : this(
        command.content,
        comment,
        user,
    )
    {
        this.id = command.id
        this.uuid = command.uuid
        this.modifiedCnt = command.modifiedCnt
        this.status = command.status
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = UUID.randomUUID().toString()
        protected set

    @Column(name = "content", nullable = false, updatable = true, columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Column(name = "modifiedCnt", nullable = false, updatable = true)
    var modifiedCnt: Int = 0
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    var status: CommentStatus = CommentStatus.SHOW
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "verificationStatus", nullable = false, updatable = true)
    var verificationStatus: VerifyStatus = VerifyStatus.NEED_VERIFICATION
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

    // Common
    fun deleteComment() {
        this.status = CommentStatus.DELETED
    }

    fun updateContent(
        content: String,
    ) {
        this.verificationStatus = VerifyStatus.NEED_VERIFICATION
        this.modifiedCnt++
        this.content = content
    }

    fun flagComment() {
        this.status = CommentStatus.FLAGGED
    }

    override fun toString(): String {
        return "Reply(id=$id, uuid='$uuid', content='$content', modifiedCnt=$modifiedCnt, status=$status, verificationStatus=$verificationStatus)"
    }
}