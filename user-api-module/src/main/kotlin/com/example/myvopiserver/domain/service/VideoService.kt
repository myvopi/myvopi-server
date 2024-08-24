package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.enums.VideoType
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.InternalVideoAndOwnerCommand
import com.example.myvopiserver.domain.command.InternalVideoCommandWithMessage
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
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
    fun searchVideoOrCreateNewWithReturnMessage(command: VideoSearchCommand): InternalVideoCommandWithMessage {
        val returnMessage = StringBuilder()
        return videoReaderStore.findVideoByTypeAndId(command.videoType, command.videoId)
            ?.let {
                returnMessage.append("Load successful")
                InternalVideoCommandWithMessage(
                    internalVideoCommand = videoMapper.to(video = it) ,
                    message = returnMessage.toString(),
                )
            }
            ?: run {
                command.internalUserCommand?.let { internalUserCommand ->
                    val requester = User(command = internalUserCommand)
                    if(requester.role == MemberRole.ROLE_UNVERIFIED)
                        throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Please verify your email address")
                    val videoCommand = Video(
                        videoId = command.videoId,
                        user = requester,
                        videoType = command.videoType,
                    )
                    val video = videoReaderStore.saveVideo(videoCommand)
                    returnMessage.append("Topic created")
                    InternalVideoCommandWithMessage(
                        internalVideoCommand = videoMapper.to(video = video),
                        message = returnMessage.toString()
                    )
                } ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video not found, you will need an account to start a topic")
            }
    }
}