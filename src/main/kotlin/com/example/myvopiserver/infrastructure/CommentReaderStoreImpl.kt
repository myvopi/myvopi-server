package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentRepository
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CommentReaderStoreImpl(
    private val commentRepository: CommentRepository,
    private val customCommentReaderStore: CustomCommentReaderStore,
): CommentReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentsFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple> {
        return customCommentReaderStore.pageableCommentAndReplyFromVideoRequest(command)
    }

    @Transactional(readOnly = true)
    override fun findCommentsFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple> {
        return customCommentReaderStore.pageableCommentAndReplyFromCommentRequest(command)
    }

    @Transactional(readOnly = true)
    override fun findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid: String): Comment? {
        return commentRepository.findWithUserAndVideoAndVideoOwnerByUuid(uuid)
    }

    @Transactional(readOnly = true)
    override fun findCommentWithUserByUuid(uuid: String): Comment? {
        return commentRepository.findWithUserByUuid(uuid)
    }

    @Transactional(readOnly = true)
    override fun findCommentByUuid(uuid: String): Comment? {
        return commentRepository.findByUuid(uuid)
    }

    @Transactional
    override fun saveComment(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    @Transactional(readOnly = true)
    override fun updateCommentStatusRequest(command: CommentUpdateRequestCommand) {
        return customCommentReaderStore.updateCommentStatusRequest(command)
    }

    @Transactional(readOnly = true)
    override fun findCommentRequest(command: SingleCommentSearchCommand): Tuple? {
        return customCommentReaderStore.findCommentRequest(command)
    }
}