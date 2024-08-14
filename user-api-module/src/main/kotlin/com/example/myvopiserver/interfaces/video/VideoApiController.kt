package com.example.myvopiserver.interfaces.video

import com.example.myvopiserver.application.video.VideoFacade
import com.authcoremodule.authentication.toUserInfo
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.enums.SearchFilter
import com.entitycoremodule.command.VideoSearchCommand
import com.entitycoremodule.info.CommentBaseInfo
import com.example.myvopiserver.common.util.CustomParser
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoApiController(
    private val customParser: CustomParser,
    private val videoFacade: VideoFacade,
) {

    @PostMapping("/{url}")
    fun searchVideo(
        authentication: Authentication?,
        @PathVariable(value = "url", required = true) url: String,
        @RequestBody body: CommentRequestDto,
    ): CommonResult<List<CommentBaseInfo>>
    {
        val urlCommand = customParser.validateAndParseVideoUrl(url)
        val userCommand = authentication?.toUserInfo()
        val searchFilter = SearchFilter.decode(body.filter)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No such filter found")
        val command = VideoSearchCommand(
            internalUserCommand = userCommand,
            videoType = urlCommand.videoType,
            videoId = urlCommand.videoId,
            filter = searchFilter,
            reqPage = body.reqPage,
        )
        val info = videoFacade.requestVideoAndComments(command)
        return CommonResponse.success(info.comments, info.message)
    }
}