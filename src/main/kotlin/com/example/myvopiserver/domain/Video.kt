package com.example.myvopiserver.domain

import jakarta.persistence.*

@Entity
class Video(
    uuid: String,       // 우리쪽 UUID
    videoId: String,    // 유튜브 UUID (비디오 아이디)
    userId: Long,       // 생성한 유저 아이디
    // TODO video title
    // TODO video author
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "uuid", nullable = false, updatable = true)
    var uuid: String = uuid
        protected set

    @Column(name = "video_id", nullable = false, updatable = true)
    var videoId: String = videoId
        protected set

    @Column(name = "created_by", nullable = false, updatable = true)
    var createdBy: Long = userId
        protected set

    @OneToMany(
        mappedBy = "video",
        fetch = FetchType.LAZY,
        targetEntity = Comment::class,
        cascade = [CascadeType.ALL],
    )
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    // TODO created by user id
}