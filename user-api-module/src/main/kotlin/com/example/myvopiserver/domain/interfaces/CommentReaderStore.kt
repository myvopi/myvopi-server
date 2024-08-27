package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.CommentsSearchCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findCommentsRequest(command: CommentsSearchCommand): List<Tuple>

    fun findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid: String): Comment?

    fun findCommentWithUserByUuid(uuid: String): Comment?

    fun findCommentByUuid(uuid: String): Comment?

    fun saveComment(comment: Comment): Comment

    fun findCommentRequest(command: SingleCommentSearchCommand): Tuple?
}