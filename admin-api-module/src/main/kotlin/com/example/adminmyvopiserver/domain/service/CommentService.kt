package com.example.adminmyvopiserver.domain.service

import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.CommentReaderStore
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
) {

    fun findCommentsByUser(command: CommentAdminSearchCommand): List<Tuple> {
        return commentReaderStore.findCommentsByUserRequest(command)
    }
}