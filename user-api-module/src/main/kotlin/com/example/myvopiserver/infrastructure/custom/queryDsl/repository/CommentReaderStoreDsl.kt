package com.example.myvopiserver.infrastructure.custom.queryDsl.repository

import com.example.myvopiserver.domain.command.CommentsSearchCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CommentReaderStoreDsl {

    fun findComments(command: CommentsSearchCommand): List<Tuple>

    fun findComment(command: SingleCommentSearchCommand): Tuple?
}