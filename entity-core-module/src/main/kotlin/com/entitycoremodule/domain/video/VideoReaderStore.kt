package com.entitycoremodule.domain.video

import com.commoncoremodule.enums.VideoType

interface VideoReaderStore {

    fun findVideoByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun findVideoWithUserByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun saveVideo(video: Video): Video
}