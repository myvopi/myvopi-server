package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Video

interface VideoReaderStore {

    fun findVideoByTypeAndId(videoType: VideoType, videoId: String): Video?

    fun saveVideo(video: Video): Video
}