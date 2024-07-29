package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.*
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findCommentsFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple>

    fun findCommentsFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple>

    fun findCommentByUuid(uuid: String): Comment?

    fun saveComment(comment: Comment): Comment

    fun updateCommentStatusRequest(command: CommentUpdateRequestCommand)

    fun findCommentRequest(command: SingleCommandSearchCommand): Tuple?
}