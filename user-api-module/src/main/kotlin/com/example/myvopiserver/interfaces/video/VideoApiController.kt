package com.example.myvopiserver.interfaces.video

import com.example.myvopiserver.application.video.VideoFacade
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.enums.SearchFilter
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.common.util.CustomParser
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoApiController(
    private val customParser: CustomParser,
    private val videoFacade: VideoFacade,
) {

    @GetMapping("/{url}")
    fun searchVideo(
        authentication: Authentication?,
        @PathVariable(value = "url", required = true) url: String,
    ): CommonResult<List<CommentBaseInfo>>
    {
        // TODO create topic cnt
        val urlCommand = customParser.validateAndParseVideoUrl(url)
        val userCommand = authentication?.toUserInfo()
        val command = VideoSearchCommand(
            internalUserCommand = userCommand,
            videoType = urlCommand.videoType,
            videoId = urlCommand.videoId,
            filter = SearchFilter.RECENT,
            reqPage = 0,
        )
        val info = videoFacade.requestVideoAndComments(command)
        return CommonResponse.success(info.comments, info.message)
    }
}