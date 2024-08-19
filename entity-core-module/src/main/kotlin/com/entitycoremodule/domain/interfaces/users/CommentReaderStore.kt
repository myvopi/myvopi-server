package com.entitycoremodule.domain.interfaces.users

import com.entitycoremodule.command.CommentSearchFromCommentCommand
import com.entitycoremodule.command.CommentSearchFromVideoCommand
import com.entitycoremodule.command.SingleCommentSearchCommand
import com.entitycoremodule.domain.comment.Comment
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findCommentsFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple>

    fun findCommentsFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple>

    fun findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid: String): Comment?

    fun findCommentWithUserByUuid(uuid: String): Comment?

    fun findCommentByUuid(uuid: String): Comment?

    fun saveComment(comment: Comment): Comment

    fun findCommentRequest(command: SingleCommentSearchCommand): Tuple?
}