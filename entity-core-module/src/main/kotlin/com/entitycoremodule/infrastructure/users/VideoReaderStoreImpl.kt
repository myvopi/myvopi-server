package com.entitycoremodule.infrastructure.users

import com.commoncoremodule.enums.VideoType
import com.entitycoremodule.domain.video.Video
import com.entitycoremodule.domain.interfaces.users.VideoReaderStore
import com.entitycoremodule.infrastructure.repository.VideoRepository
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