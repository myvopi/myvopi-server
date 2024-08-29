package com.example.adminmyvopiserver.infrastructure

import com.example.adminmyvopiserver.domain.Comment
import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.interfaces.CommentReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.example.adminmyvopiserver.infrastructure.repository.CommentRepository
import com.querydsl.core.Tuple
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class CommentReaderStoreImpl(
    private val customCommentReaderStore: CustomCommentReaderStore,
    private val commentRepository: CommentRepository,
): CommentReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple> {
        return customCommentReaderStore.findCommentsByUserRequest(command)
    }

    @Transactional(readOnly = true)
    override fun findCommentsBetweenDate(fromDate: LocalDateTime, toDate: LocalDateTime): List<Comment> {
        return commentRepository.findByCreatedDtBetween(fromDate, toDate)
    }

    @Transactional(readOnly = true)
    override fun findCommentsBetweenDateRequest(fromDate: LocalDateTime, toDate: LocalDateTime, reqPage: Int): List<Tuple> {
        return customCommentReaderStore.findCommentsBetweenDateRequest(fromDate, toDate, reqPage)
    }

    @Transactional
    override fun updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand) {
        customCommentReaderStore.updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand)
    }

    @Transactional(readOnly = true)
    override fun findCommentsByUser(user: User, pageable: Pageable): List<Comment> {
        return commentRepository.findByUser(user, pageable)
    }
}