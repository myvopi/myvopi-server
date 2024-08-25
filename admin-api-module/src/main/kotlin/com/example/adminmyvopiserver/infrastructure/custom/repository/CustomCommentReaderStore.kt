package com.example.adminmyvopiserver.infrastructure.custom.repository

import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.querydsl.core.Tuple
import java.time.LocalDateTime

interface CustomCommentReaderStore {

    fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple>

    fun findCommentsBetweenDateRequest(fromDate: LocalDateTime, toDate: LocalDateTime, reqPage: Int): List<Tuple>

    fun updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand)
}