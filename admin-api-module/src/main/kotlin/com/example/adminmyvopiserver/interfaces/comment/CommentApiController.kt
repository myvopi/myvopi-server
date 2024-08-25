package com.example.adminmyvopiserver.interfaces.comment

import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.adminmyvopiserver.application.comment.CommentFacade
import com.example.adminmyvopiserver.domain.info.CommentBaseInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/v1/comment")
class CommentApiController(
    private val commentFacade: CommentFacade,
) {

    @Secured("ROLE_ADMIN")
    @GetMapping("/today")
    fun getTodayComments(
        @RequestParam("reqPage", required = true, defaultValue = "0") reqPage: Int,
    ): CommonResult<List<CommentBaseInfo>> {
        val info = commentFacade.requestTodayComments(reqPage)
        return CommonResponse.success(info)
    }

    // TODO get comments by user
    // @GetMapping

    // TODO get find comments

    // TODO verify comment

    // TODO update comment status
}