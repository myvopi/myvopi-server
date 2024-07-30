package com.example.myvopiserver.domain

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.domain.role.User
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name ="comment")
class Comment(
    content: String,           // 내용
    video: Video,              // 상위 비디오
    user: User,                // 생성자
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

    override fun toString(): String {
        return "Comment(id=$id, uuid='$uuid', content='$content', modifiedCnt=$modifiedCnt, status=$status, video=$video, replies=$replies, likes=$likes, user=$user)"
    }

    fun updateContent(
        content: String,
    ) {
        if(this.content == content) return
        // TODO verify request to admin
        this.modifiedCnt++
        this.content = content
    }

    fun deleteComment() {
        this.status = CommentStatus.DELETED
    }

    // TODO verified
}
