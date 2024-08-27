package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.command.CommentsSearchCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomCommentReaderStore {

    fun findCommentsRequest(command: CommentsSearchCommand): List<Tuple>

    fun findCommentRequest(command: SingleCommentSearchCommand): Tuple?
}