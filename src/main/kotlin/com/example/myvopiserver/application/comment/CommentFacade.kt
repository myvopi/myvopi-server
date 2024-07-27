package com.example.myvopiserver.application.comment

import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
) {

    fun requestComments(command: CommentSearchFromCommentCommand): List<CommentBaseInfo> {
        val result = commentService.findComments(command)
        return commentService.constructCommentBaseInfo(result)
    }
}