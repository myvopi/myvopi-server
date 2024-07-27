package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentRepository
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
class CommentReaderStoreImpl(
    private val commentRepository: CommentRepository,
    private val customCommentReaderStore: CustomCommentReaderStore,
): CommentReaderStore {

    override fun findCommentsFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple> {
        return customCommentReaderStore.pageableCommentAndReplyFromVideoRequest(command)
    }

    override fun findCommentsFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple> {
        return customCommentReaderStore.pageableCommentAndReplyFromCommentRequest(command)
    }
}