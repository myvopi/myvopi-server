package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.infrastructure.repository.VideoRepository
import org.springframework.stereotype.Repository

@Repository
class VideoReaderStoreImpl(
   private val videoRepository: VideoRepository,
): VideoReaderStore {
}