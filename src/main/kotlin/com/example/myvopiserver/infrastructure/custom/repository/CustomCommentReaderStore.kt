package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.command.CommentSearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomCommentReaderStore {

    fun pageableCommentAndReplyFindByVideo(command: CommentSearchCommand): List<Tuple>
}