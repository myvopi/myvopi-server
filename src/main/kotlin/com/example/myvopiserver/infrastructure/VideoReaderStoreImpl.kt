package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.infrastructure.repository.VideoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class VideoReaderStoreImpl(
   private val videoRepository: VideoRepository,
): VideoReaderStore {

   @Transactional(readOnly = true)
   override fun findVideoByTypeAndId(videoType: VideoType, videoId: String): Video? {
      return videoRepository.findByVideoTypeAndVideoId(videoType, videoId)
   }

   @Transactional
   override fun saveVideo(video: Video): Video {
      return videoRepository.save(video)
   }
}