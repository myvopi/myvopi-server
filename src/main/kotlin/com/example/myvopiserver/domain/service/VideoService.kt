package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.InternalVideoCommand
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.VideoMapper
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoReaderStore: VideoReaderStore,
    private val videoMapper: VideoMapper,
    private val userReaderStore: UserReaderStore,
) {


    // Db-transactions (readOnly)
    fun findByTypeAndId(videoType: VideoType, videoId: String): InternalVideoCommand {
        val video = videoReaderStore.findVideoByTypeAndId(videoType, videoId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return videoMapper.to(video = video)
    }

    // Db-transactions
    fun createNewVideo(
        videoId: String,
        userId: String,
        videoType: VideoType,
    ): InternalVideoCommand
    {
        val user = userReaderStore.findUserByUserId(userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val videoCommand = Video(
            videoId = videoId,
            user = user,
            videoType = videoType,
        )
        val video = videoReaderStore.saveVideo(videoCommand)
        return videoMapper.to(video = video)
    }

    fun searchVideoOrCreateNew(command: VideoSearchCommand): InternalVideoCommand {
        return videoReaderStore.findVideoByTypeAndId(command.videoType, command.videoId)
            ?.let { videoMapper.to(video = it) }
            ?: run {
                command.internalUserCommand?.let { internalUserCommand ->
                    createNewVideo(
                        videoId = command.videoId,
                        userId = internalUserCommand.userId,
                        videoType = command.videoType,
                    )
                } ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video not found, you will need an account to start a topic")
            }
    }
}