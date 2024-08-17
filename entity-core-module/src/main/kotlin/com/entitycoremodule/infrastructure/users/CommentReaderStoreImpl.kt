package com.entitycoremodule.infrastructure.users

import com.entitycoremodule.command.CommentSearchFromCommentCommand
import com.entitycoremodule.command.CommentSearchFromVideoCommand
import com.entitycoremodule.command.SingleCommentSearchCommand
import com.entitycoremodule.domain.comment.Comment
import com.entitycoremodule.domain.interfaces.users.CommentReaderStore
import com.entitycoremodule.infrastructure.users.custom.repository.CustomCommentReaderStore
import com.entitycoremodule.infrastructure.repository.CommentRepository
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
    override fun findCommentRequest(command: SingleCommentSearchCommand): Tuple? {
        return customCommentReaderStore.findCommentRequest(command)
    }
}