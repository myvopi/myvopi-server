package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Video
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: JpaRepository<Video, Long> {

    fun findByVideoTypeAndVideoId(videoType: VideoType, videoId: String): Video?

    @EntityGraph(attributePaths = ["user"])
    fun findWithUserByVideoTypeAndVideoId(videoType: VideoType, videoId: String): Video?
}