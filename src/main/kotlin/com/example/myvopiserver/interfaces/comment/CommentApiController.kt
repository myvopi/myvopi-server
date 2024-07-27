package com.example.myvopiserver.interfaces.comment

import com.example.myvopiserver.application.comment.CommentFacade
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.config.response.CommonResponse
import com.example.myvopiserver.common.config.response.CommonResult
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
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

    // TODO update comment and delete comment

}