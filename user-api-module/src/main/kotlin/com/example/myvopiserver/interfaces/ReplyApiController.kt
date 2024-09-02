package com.example.myvopiserver.interfaces

import com.example.myvopiserver.application.reply.ReplyFacade
import com.commoncoremodule.enums.ReportType
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.extension.parsePreferences
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import com.example.myvopiserver.interfaces.dto.reply.*
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
class ReplyApiController(
    private val replyFacade: ReplyFacade,
) {

    @GetMapping(path = ["/op/api/v1/reply"])
    fun getReplies(
        authentication: Authentication?,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestParam(value = "commentUuid", required = true) commentUuid: String,
        @RequestParam(value = "reqPage", required = true) reqPage: Int,
    ): CommonResult<List<ReplyBaseInfo>>
    {
        val userCommand = authentication?.toUserInfo()
        val command = ReplySearchCommand(
            internalUserCommand = userCommand,
            commentUuid = commentUuid,
            reqPage = reqPage,
            preferences = parsePreferences(cookie),
        )
        val info = replyFacade.requestReplies(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PutMapping(path = ["/cv/api/v1/reply"])
    fun updateReply(
        authentication: Authentication,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestBody body: ReplyUpdateDto,
    ): CommonResult<ReplyBaseInfo>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyUpdateCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            replyUuid = body.uuid,
            preferences = parsePreferences(cookie),
        )
        val info = replyFacade.requestReplyUpdate(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @PostMapping(path = ["/cv/api/v1/reply"])
    fun postReply(
        authentication: Authentication,
        @RequestHeader("cookie", required = true) cookie: String,
        @RequestBody body: ReplyPostDto,
    ): CommonResult<ReplyBaseInfo>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = ReplyPostCommand(
            internalUserCommand = internalUserCommand,
            content = body.content,
            commentUuid = body.commentUuid,
            preferences = parsePreferences(cookie),
        )
        val info = replyFacade.requestReplyPost(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = ["/cv/api/v1/reply"])
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
    @PostMapping(path = ["/cv/api/v1/reply/like"])
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
    @PostMapping(path = ["/cv/api/v1/reply/unlike"])
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
    @PostMapping(path = ["/cv/api/v1/reply/report"])
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