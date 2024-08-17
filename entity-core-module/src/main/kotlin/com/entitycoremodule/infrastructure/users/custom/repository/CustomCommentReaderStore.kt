package com.entitycoremodule.infrastructure.users.custom.repository

import com.entitycoremodule.command.CommentSearchFromCommentCommand
import com.entitycoremodule.command.CommentSearchFromVideoCommand
import com.entitycoremodule.command.SingleCommentSearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomCommentReaderStore {

    fun pageableCommentAndReplyFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple>

    fun pageableCommentAndReplyFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple>

    fun findCommentRequest(command: SingleCommentSearchCommand): Tuple?
}