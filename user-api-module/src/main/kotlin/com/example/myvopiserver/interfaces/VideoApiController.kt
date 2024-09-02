package com.example.myvopiserver.interfaces

import com.example.myvopiserver.application.video.VideoFacade
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType
import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.ErrorCode
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.commoncoremodule.extension.parsePreferences
import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoApiController(
    private val videoFacade: VideoFacade,
) {

    @GetMapping(path = ["/watch"])
    fun searchVideo(
        authentication: Authentication?,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestParam(value = "v", required = false) ytv: String?,
        @RequestParam(value = "s", required = false) yts: String?,
    ): CommonResult<List<CommentBaseInfo>>
    {
        if(ytv.isNullOrBlank() && yts.isNullOrBlank()) throw BadRequestException(ErrorCode.BAD_REQUEST, "Must specify ?v= or ?s=")
        val userCommand = authentication?.toUserInfo()
        val command = VideoSearchCommand(
            internalUserCommand = userCommand,
            videoType = ytv?.let { VideoType.YT_VIDEO } ?: run { VideoType.YT_SHORT },
            videoId = ytv?.let { ytv } ?: run { yts!! },
            filter = SearchFilter.RECENT,
            reqPage = 0,
            preferences = parsePreferences(cookie),
        )
        val info = videoFacade.requestVideoAndComments(command)
        return CommonResponse.success(info.comments, info.message)
    }
}