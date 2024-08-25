package com.example.adminmyvopiserver.domain.interfaces

import com.example.adminmyvopiserver.domain.Comment
import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.querydsl.core.Tuple
import java.time.LocalDateTime

interface CommentReaderStore {

    fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple>

    fun findCommentsBetweenDate(fromDate: LocalDateTime, toDate: LocalDateTime): List<Comment>

    fun findCommentsBetweenDateRequest(fromDate: LocalDateTime, toDate: LocalDateTime, reqPage: Int): List<Tuple>

    fun updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand)
}