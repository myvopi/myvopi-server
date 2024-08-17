package com.entitycoremodule.infrastructure.repository

import com.commoncoremodule.enums.VideoType
import com.entitycoremodule.domain.video.Video
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: JpaRepository<Video, Long> {

    fun findByVideoTypeAndVideoId(videoType: VideoType, videoId: String): Video?

    @EntityGraph(attributePaths = ["user"])
    fun findWithUserByVideoTypeAndVideoId(videoType: VideoType, videoId: String): Video?
}