package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
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