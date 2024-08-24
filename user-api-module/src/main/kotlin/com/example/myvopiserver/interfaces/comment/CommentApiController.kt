package com.example.myvopiserver.interfaces.comment

import com.commoncoremodule.enums.ReportType
import com.example.myvopiserver.application.comment.CommentFacade
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comment")
class CommentApiController(
    private val commentFacade: CommentFacade,
) {

    @GetMapping("/")
    fun getComments(
        authentication: Authentication?,
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
        val command = CommentSearchFromCommentCommand(
            internalUserCommand = userCommand,
            filter = searchFilter,
            reqPage = reqPage,
            videoId = videoId,
            videoType = videoTypeEnum,
        )
        val info = commentFacade.requestComments(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PutMapping("/")
    fun updateComment(
        authentication: Authentication,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentUpdateDto,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentUpdateCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        val info = commentFacade.requestCommentUpdate(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PostMapping("/")
    fun postComment(
        authentication: Authentication,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentPostDto,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentPostCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        val info = commentFacade.requestCommentPost(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/")
    fun deleteComment(
        authentication: Authentication,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentDeleteDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentDeleteCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        commentFacade.requestCommentDelete(command)
        return CommonResponse.success("Comment deleted")
    }

    @Secured("ROLE_USER")
    @PostMapping("/like")
    fun postLike(
        authentication: Authentication,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentLikeDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentLikeCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        commentFacade.requestCommentLike(command)
        return CommonResponse.success("Comment liked")
    }

    @Secured("ROLE_USER")
    @PostMapping("/unlike")
    fun postUnlike(
        authentication: Authentication,
        @RequestParam(value = "videoType", required = true) videoType: String,
        @RequestParam(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentLikeDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentLikeCommand(
            internalUserCommand = internalUserCommand,
            commentUuid = body.uuid,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        commentFacade.requestCommentUnlike(command)
        return CommonResponse.success("Comment unliked")
    }

    @Secured("ROLE_USER")
    @PostMapping("/report")
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