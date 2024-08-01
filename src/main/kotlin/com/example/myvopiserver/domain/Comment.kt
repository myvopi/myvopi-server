package com.example.myvopiserver.domain

import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.domain.command.InternalCommentCommand
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name ="comment")
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
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = UUID.randomUUID().toString()
        protected set

    @Column(name = "content", nullable = false, updatable = true, columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Column(name = "modified_cnt", nullable = false, updatable = true)
    var modifiedCnt: Int = 0
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status", nullable = false, updatable = true)
    var status: CommentStatus = CommentStatus.SHOW
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
        cascade = [CascadeType.ALL],
    )
    var replies: MutableList<Reply> = mutableListOf()
        protected set

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        targetEntity = CommentLike::class,
        cascade = [CascadeType.ALL],
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

    fun updateContent(
        content: String,
    ) {
        if(this.status == CommentStatus.DELETED) throw BadRequestException(ErrorCode.BAD_REQUEST, "This comment has already been deleted")
        if(this.content == content) return
        // TODO verify request to admin
        this.modifiedCnt++
        this.content = content
    }

    fun deleteComment() {
        if(this.status == CommentStatus.DELETED) throw BadRequestException(ErrorCode.BAD_REQUEST, "This comment has already been deleted")
        this.status = CommentStatus.DELETED
    }

    fun validateOwnerAndRequester(command: InternalUserCommand) {
        if(command.uuid != user.uuid || command.userId != user.userId) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Not allowed to request any actions for this comment")
        }
    }

    override fun toString(): String {
        return "Comment(id=$id, uuid='$uuid', content='$content', modifiedCnt=$modifiedCnt, status=$status)"
    }

    // TODO verified
}
