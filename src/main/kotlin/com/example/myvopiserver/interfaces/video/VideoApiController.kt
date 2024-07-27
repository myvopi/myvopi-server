package com.example.myvopiserver.interfaces.video

import com.example.myvopiserver.application.video.VideoFacade
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.config.response.CommonResponse
import com.example.myvopiserver.common.config.response.CommonResult
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoApiController(
    private val videoFacade: VideoFacade,
) {

    @PostMapping("/{videoType}={videoId}")
    fun searchVideo(
        authentication: Authentication?,
        @PathVariable(value = "videoType", required = true) videoType: String,
        @PathVariable(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentRequestDto,
    ): CommonResult<List<CommentBaseInfo>>
    {
        val userCommand = authentication?.toUserInfo()
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val searchFilter = SearchFilter.decode(body.filter)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No such filter found")
        val command = VideoSearchCommand(
            authenticationState = userCommand?.let { true } ?: false,
            internalUserCommand = userCommand,
            videoType = videoTypeEnum,
            videoId = videoId,
            filter = searchFilter,
            reqPage = body.reqPage,
        )
        val info = videoFacade.requestVideoAndComments(command)
        return CommonResponse.success(info)
    }
}