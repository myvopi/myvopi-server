package com.entitycoremodule.infrastructure.video

import com.commoncoremodule.enums.VideoType
import com.entitycoremodule.domain.video.Video
import com.entitycoremodule.domain.video.VideoReaderStore
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

   @Transactional(readOnly = true)
   override fun findVideoWithUserByTypeAndId(videoType: VideoType, videoId: String): Video? {
      return videoRepository.findWithUserByVideoTypeAndVideoId(videoType, videoId)
   }

   @Transactional
   override fun saveVideo(video: Video): Video {
      return videoRepository.save(video)
   }
}