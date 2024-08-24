package com.example.adminmyvopiserver.infrastructure

import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.CommentReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CommentReaderStoreImpl(
    private val customCommentReaderStore: CustomCommentReaderStore,
): CommentReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple> {
        return customCommentReaderStore.findCommentsByUserRequest(command)
    }
}