package com.example.adminmyvopiserver.infrastructure.custom.repository

import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.querydsl.core.Tuple

interface CustomCommentReaderStore {

    fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple>
}