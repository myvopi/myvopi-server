package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.InternalVideoAndOwnerCommand
import com.example.myvopiserver.domain.command.InternalVideoCommand
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
import com.example.myvopiserver.domain.role.User
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoReaderStore: VideoReaderStore,
    private val videoMapper: VideoMapper,
    private val userMapper: UserMapper,
) {

    // Db-transactions (readOnly)
    fun findVideoWithOwner(videoType: VideoType, videoId: String): InternalVideoAndOwnerCommand {
        val video = videoReaderStore.findVideoWithUserByTypeAndId(videoType, videoId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val owner = video.user
        return InternalVideoAndOwnerCommand(
            internalVideoCommand = videoMapper.to(video = video),
            internalUserCommand = userMapper.to(user = owner)!!,
        )
    }

    // Db-transactions
    fun searchVideoOrCreateNew(command: VideoSearchCommand): InternalVideoCommand {
        return videoReaderStore.findVideoByTypeAndId(command.videoType, command.videoId)
            ?.let { videoMapper.to(video = it) }
            ?: run {
                command.internalUserCommand?.let { internalUserCommand ->
                    val requester = User(command = internalUserCommand)
                    val videoCommand = Video(
                        videoId = command.videoId,
                        user = requester,
                        videoType = command.videoType,
                    )
                    val video = videoReaderStore.saveVideo(videoCommand)
                    videoMapper.to(video = video)
                } ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video not found, you will need an account to start a topic")
            }
    }
}