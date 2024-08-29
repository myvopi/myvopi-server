package com.example.myvopiserver.interfaces.reply

import com.example.myvopiserver.application.reply.ReplyFacade
import com.commoncoremodule.enums.ReportType
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReplyApiController(
    private val replyFacade: ReplyFacade,
) {

    @GetMapping("/op/api/v1/reply")
    fun getReplies(
        authentication: Authentication?,
        @RequestParam(value = "commentUuid", required = true) commentUuid: String,
        @RequestParam(value = "reqPage", required = true) reqPage: Int,
    ): CommonResult<List<ReplyBaseInfo>>
    {
        val userCommand = authentication?.toUserInfo()
        val command = ReplySearchCommand(
            internalUserCommand = userCommand,
            commentUuid = commentUuid,
            reqPage = reqPage,
        )
        val info = replyFacade.requestReplies(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PutMapping("/cv/api/v1/reply")
    fun updateReply(
        authentication: Authentication,
        @RequestBody body: ReplyUpdateDto,
    ): CommonResult<ReplyBaseInfo>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyUpdateCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            replyUuid = body.uuid,
        )
        val info = replyFacade.requestReplyUpdate(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PostMapping("/cv/api/v1/reply")
    fun postReply(
        authentication: Authentication,
        @RequestBody body: ReplyPostDto,
    ): CommonResult<ReplyBaseInfo>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyPostCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            commentUuid = body.commentUuid,
        )
        val info = replyFacade.requestReplyPost(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/cv/api/v1/reply")
    fun deleteReply(
        authentication: Authentication,
        @RequestBody body: ReplyDeleteDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyDeleteCommand(
            internalUserCommand = internalUserCommand,
            replyUuid = body.uuid,
        )
        replyFacade.requestReplyDelete(command)
        return CommonResponse.success("Reply deleted")
    }

    @Secured("ROLE_USER")
    @PostMapping("/cv/api/v1/reply/like")
    fun postReplyLike(
        authentication: Authentication,
        @RequestBody body: ReplyLikeDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyLikeCommand(
            internalUserCommand = internalUserCommand,
            replyUuid = body.uuid,
        )
        replyFacade.requestReplyLike(command)
        return CommonResponse.success("Reply liked")
    }

    @Secured("ROLE_USER")
    @PostMapping("/cv/api/v1/reply/unlike")
    fun postReplyUnlike(
        authentication: Authentication,
        @RequestBody body: ReplyLikeDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyLikeCommand(
            internalUserCommand = internalUserCommand,
            replyUuid = body.uuid,
        )
        replyFacade.requestReplyUnlike(command)
        return CommonResponse.success("Reply unliked")
    }

    @Secured("ROLE_USER")
    @PostMapping("/cv/api/v1/reply/report")
    fun postReport(
        authentication: Authentication,
        @RequestBody body: ReplyReportDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val reportType = ReportType.decode(body.reportType)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Report type not provided")
        val command = ReplyReportCommand(
            internalUserCommand = internalUserCommand,
            replyUuid = body.uuid,
            reportType = reportType,
        )
        replyFacade.requestReportReply(command)
        return CommonResponse.success("Reported")
    }
}