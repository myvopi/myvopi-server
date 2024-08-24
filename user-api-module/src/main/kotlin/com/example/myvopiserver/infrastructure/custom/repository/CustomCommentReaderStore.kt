package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomCommentReaderStore {

    fun pageableCommentAndReplyFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple>

    fun pageableCommentAndReplyFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple>

    fun findCommentRequest(command: SingleCommentSearchCommand): Tuple?
}