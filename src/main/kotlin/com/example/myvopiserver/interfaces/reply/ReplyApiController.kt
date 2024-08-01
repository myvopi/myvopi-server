package com.example.myvopiserver.interfaces.reply

import com.example.myvopiserver.application.reply.ReplyFacade
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.common.config.response.CommonResponse
import com.example.myvopiserver.common.config.response.CommonResult
import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reply")
class ReplyApiController(
    private val replyFacade: ReplyFacade,
) {

    @GetMapping("/")
    fun getComments(
        authentication: Authentication?,
        @RequestParam(value = "uuid", required = true) uuid: String,
        @RequestParam(value = "reqPage", required = true) reqPage: Int,
    ): CommonResult<List<ReplyBaseInfo>>
    {
        val userCommand = authentication?.toUserInfo()
        val command = ReplySearchCommand(
            internalUserCommand = userCommand,
            commentUuid = uuid,
            reqPage = reqPage,
        )
        val info = replyFacade.requestReplies(command)
        return CommonResponse.success(info)
    }
}