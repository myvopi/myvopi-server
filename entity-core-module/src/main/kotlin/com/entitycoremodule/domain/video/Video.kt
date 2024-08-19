package com.entitycoremodule.domain.video

import com.commoncoremodule.enums.VideoType
import com.entitycoremodule.command.InternalVideoCommand
import com.entitycoremodule.domain.comment.Comment
import com.entitycoremodule.domain.BaseTime
import com.entitycoremodule.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "video")
class Video(
    videoId: String,     // 유튜브 UUID (비디오 아이디)
    user: User,          // 생성한 유저 아이디
    videoType: VideoType // 영상 타입
): BaseTime() {

    constructor(
        command: InternalVideoCommand,
        user: User
    ) : this(
        command.videoId,
        user,
        command.videoType,
    )
    {
        this.id = command.id
        this.uuid = command.uuid
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = UUID.randomUUID().toString()
        protected set

    @Column(name = "video_id", nullable = false, updatable = true)
    var videoId: String = videoId
        protected set

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = User::class,
    )
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "video_type", nullable = false, updatable = true)
    var videoType: VideoType = videoType
        protected set

    @OneToMany(
        mappedBy = "video",
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
        cascade = [CascadeType.ALL],
    )
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    override fun toString(): String {
        return "Video(id=$id, uuid='$uuid', videoId='$videoId', videoType=$videoType)"
    }
}