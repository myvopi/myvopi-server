package com.example.myvopiserver.interfaces.comment

import com.example.myvopiserver.application.comment.CommentFacade
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.config.response.CommonResponse
import com.example.myvopiserver.common.config.response.CommonResult
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.command.CommentDeleteCommand
import com.example.myvopiserver.domain.command.CommentPostCommand
import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentUpdateCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comment")
class CommentApiController(
    private val commentFacade: CommentFacade,
) {

    @GetMapping("/{videoType}={videoId}")
    fun getComments(
        authentication: Authentication?,
        @PathVariable(value = "videoType", required = true) videoType: String,
        @PathVariable(value = "videoId", required = true) videoId: String,
        @RequestParam(value = "filter", required = true) filter: String,
        @RequestParam(value = "reqPage", required = true) reqPage: Int,
    ): CommonResult<List<CommentBaseInfo>>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val searchFilter = SearchFilter.decode(filter)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No such filter found")
        val command = CommentSearchFromCommentCommand(
            filter = searchFilter,
            reqPage = reqPage,
            videoId = videoId,
            videoType = videoTypeEnum,
        )
        val info = commentFacade.requestComments(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PutMapping("/{videoType}={videoId}")
    fun updateComment(
        authentication: Authentication,
        @PathVariable(value = "videoType", required = true) videoType: String,
        @PathVariable(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentUpdateDto,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentUpdateCommand(
            internalUserInfo = internalUserCommand,
            content = body.content,
            commentUuid = body.commentUuid,
            userId = body.userId,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        val info = commentFacade.requestCommentUpdate(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PostMapping("/{videoType}={videoId}")
    fun postComment(
        authentication: Authentication,
        @PathVariable(value = "videoType", required = true) videoType: String,
        @PathVariable(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentPostDto,
    ): CommonResult<CommentBaseInfo>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentPostCommand(
            internalUserInfo = internalUserCommand,
            content = body.content,
            videoType = videoTypeEnum,
            videoId = videoId,
        )
        val info = commentFacade.requestCommentPost(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{videoType}={videoId}")
    fun deleteComment(
        authentication: Authentication,
        @PathVariable(value = "videoType", required = true) videoType: String,
        @PathVariable(value = "videoId", required = true) videoId: String,
        @RequestBody body: CommentDeleteDto,
    ): CommonResult<String>
    {
        val videoTypeEnum = VideoType.decode(videoType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val internalUserCommand = authentication.toUserInfo()
        val command = CommentDeleteCommand(
            internalUserInfo = internalUserCommand,
            commentUuid = body.commentUuid,
            videoType = videoTypeEnum,
            videoId = videoId,
            userId = body.userId,
        )
        commentFacade.requestCommentDelete(command)
        return CommonResponse.success("Comment deleted")
    }
}