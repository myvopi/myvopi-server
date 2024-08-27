package com.example.myvopiserver.domain

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.VerifyStatus
import com.example.myvopiserver.domain.command.InternalCommentCommand
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.util.UUID

@Entity
@Table(name ="comment")
@DynamicUpdate
@DynamicInsert
class Comment(
    content: String,           // 내용
    user: User,                // 생성자
    video: Video,              // 상위 비디오
): BaseTime() {

    constructor(
        command: InternalCommentCommand,
        user: User,
        video: Video,
    ) : this(
        command.content,
        user,
        video,
    )
    {
        this.id = command.id
        this.uuid = command.uuid
        this.modifiedCnt = command.modifiedCnt
        this.status = command.status
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
        targetEntity = Video::class,
    )
    @JoinColumn(name = "video_id", nullable = false)
    var video: Video = video
        protected set

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        targetEntity = Reply::class,
    )
    var replies: MutableList<Reply> = mutableListOf()
        protected set

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        targetEntity = CommentLike::class,
    )
    var likes: MutableList<CommentLike> = mutableListOf()
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
        return "Comment(id=$id, uuid='$uuid', content='$content', modifiedCnt=$modifiedCnt, status=$status, verificationStatus=$verificationStatus)"
    }
}