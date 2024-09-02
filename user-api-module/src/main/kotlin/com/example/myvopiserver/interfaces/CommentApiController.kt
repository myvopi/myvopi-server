package com.example.myvopiserver.interfaces

import com.commoncoremodule.enums.ReportType
import com.example.myvopiserver.application.comment.CommentFacade
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType
import com.commoncoremodule.extension.parsePreferences
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.interfaces.dto.comment.*
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
class CommentApiController(
    private val commentFacade: CommentFacade,
) {

    @GetMapping(path = ["/op/api/v1/comment"])
    fun getComments(
        authentication: Authentication?,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestParam(value = "filter", required = true) filter: String,
        @RequestParam(value = "reqPage", required = true) reqPage: Int,
    ): CommonResult<List<CommentBaseInfo>>
    {
        val userCommand = authentication?.toUserInfo()
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val searchFilter = SearchFilter.decode(filter)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No such filter found")
        val command = CommentsSearchCommand(
            internalUserCommand = userCommand,
            filter = searchFilter,
            reqPage = reqPage,
            videoId = videoId,
            videoType = videoTypeEnum,
            preferences = parsePreferences(cookie),
        )
        val info = commentFacade.requestComments(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PutMapping(path = ["/cv/api/v1/comment"])
    fun updateComment(
        authentication: Authentication,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestBody body: CommentUpdateDto,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(body.videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentUpdateCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = body.videoId,
            preferences = parsePreferences(cookie),
        )
        val info = commentFacade.requestCommentUpdate(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PostMapping(path = ["/cv/api/v1/comment"])
    fun postComment(
        authentication: Authentication,
        @RequestBody body: CommentPostDto,
        @RequestHeader("cookie", required = true) cookie: String,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(body.videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentPostCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            videoType = videoTypeEnum,
            videoId = body.videoId,
            preferences = parsePreferences(cookie),
        )
        val info = commentFacade.requestCommentPost(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = ["/cv/api/v1/comment"])
    fun deleteComment(
        authentication: Authentication,
        @RequestBody body: CommentDeleteDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(body.videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentDeleteCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = body.videoId,
        )
        commentFacade.requestCommentDelete(command)
        return CommonResponse.success("Comment deleted")
    }

    @Secured("ROLE_USER")
    @PostMapping(path = ["/cv/api/v1/comment/like"])
    fun postLike(
        authentication: Authentication,
        @RequestBody body: CommentLikeDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(body.videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentLikeCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = body.videoId,
        )
        commentFacade.requestCommentLike(command)
        return CommonResponse.success("Comment liked")
    }

    @Secured("ROLE_USER")
    @PostMapping(path = ["/cv/api/v1/comment/unlike"])
    fun postUnlike(
        authentication: Authentication,
        @RequestBody body: CommentLikeDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(body.videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentLikeCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = body.videoId,
        )
        commentFacade.requestCommentUnlike(command)
        return CommonResponse.success("Comment unliked")
    }

    @Secured("ROLE_USER")
    @PostMapping(path = ["/cv/api/v1/comment/report"])
    fun postReport(
        authentication: Authentication,
        @RequestBody body: CommentReportDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val reportType = ReportType.decode(body.reportType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Report type not provided")
        val command = CommentReportCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            reportType = reportType,
        )
        commentFacade.requestReportComment(command)
        return CommonResponse.success("Reported")
    }
}