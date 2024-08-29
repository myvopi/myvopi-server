package com.example.adminmyvopiserver.application.comment

import com.example.adminmyvopiserver.domain.info.CommentBaseInfo
import com.example.adminmyvopiserver.domain.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
) {

    fun requestTodayComments(reqPage: Int): List<CommentBaseInfo> {
        val results = commentService.getTodayCommentsRequest(reqPage)
        return commentService.mapCommentBaseInfoOfResults(results)
    }
}