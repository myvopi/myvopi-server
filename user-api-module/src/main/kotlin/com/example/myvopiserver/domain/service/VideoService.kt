package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.enums.VideoType
import com.example.myvopiserver.domain.QUser
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoReaderStore: VideoReaderStore,
    private val videoMapper: VideoMapper,
    private val userMapper: UserMapper,
    private val validationService: ValidationService,
    private val userReaderStore: UserReaderStore,
) {

    // Db-transactions (readOnly)
    fun getVideoWithOwner(videoType: VideoType, videoId: String): InternalVideoAndOwnerCommand {
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
        val video = videoReaderStore.findVideoByTypeAndId(command.videoType, command.videoId)
        // 동영상 주제가 존재 하지 않을시
        return if(video == null) {
            // 인증 정보가 첨부 되었을시
            command.internalUserCommand?.let { internalUserCommand ->
                // 사용자 토픽 생성 횟수 검사
                validationService.validateIfDailyChanceExceeded(internalUserCommand.dailyChance)
                val newDailyChance = internalUserCommand.dailyChance - 1
                userReaderStore.updateUserRequest(
                    internalUserCommand,
                    // 동적 쿼리 생성
                    listOf(UpdateClauseCommand(pathName = QUser.user.dailyChance.metadata.name, value = newDailyChance)),
                )
                val requester = User(command = internalUserCommand)
                val videoCommand = Video(
                    videoId = command.videoId,
                    user = requester,
                    videoType = command.videoType,
                )
                val savedVideo = videoReaderStore.saveVideo(videoCommand)
                returnMessage.append("Topic created")
                InternalVideoCommandWithMessage(
                    internalVideoCommand = videoMapper.to(video = savedVideo),
                    message = returnMessage.toString(),
                    search = false,
                )
            } ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video not found, you will need an account to start a topic")
        } else {
            returnMessage.append("Load successful")
            InternalVideoCommandWithMessage(
                internalVideoCommand = videoMapper.to(video = video) ,
                message = returnMessage.toString(),
                search = true,
            )
        }
    }
}