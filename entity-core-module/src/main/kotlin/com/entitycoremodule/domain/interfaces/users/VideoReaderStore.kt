package com.entitycoremodule.domain.interfaces.users

import com.commoncoremodule.enums.VideoType
import com.entitycoremodule.domain.video.Video

interface VideoReaderStore {

    fun findVideoByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun findVideoWithUserByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun saveVideo(video: Video): Video
}