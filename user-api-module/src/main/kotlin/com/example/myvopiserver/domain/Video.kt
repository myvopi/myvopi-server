package com.example.myvopiserver.domain

import com.commoncoremodule.enums.VideoType
import com.example.myvopiserver.domain.command.InternalVideoCommand
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@Table(name = "video")
@DynamicInsert
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

    @Column(name = "videoId", nullable = false, updatable = true)
    var videoId: String = videoId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "videoType", nullable = false, updatable = true)
    var videoType: VideoType = videoType
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        protected set

    @OneToMany(
        mappedBy = "video",
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
    )
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    override fun toString(): String {
        return "Video(id=$id, uuid='$uuid', videoId='$videoId', videoType=$videoType)"
    }
}