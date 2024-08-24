package com.example.adminmyvopiserver.domain.interfaces

import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple>
}