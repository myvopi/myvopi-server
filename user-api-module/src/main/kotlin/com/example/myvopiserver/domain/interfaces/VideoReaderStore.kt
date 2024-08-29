package com.example.myvopiserver.domain.interfaces

import com.commoncoremodule.enums.VideoType
import com.example.myvopiserver.domain.Video

interface VideoReaderStore {

    // unused
    fun findVideoByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun findVideoWithUserByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun saveVideo(video: Video): Video
}