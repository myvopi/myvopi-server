package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.command.CommentSearchCommand
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

    override fun findComments(command: CommentSearchCommand): List<Tuple> {
        return customCommentReaderStore.pageableCommentAndReplyFindByVideo(command)
    }
}