package com.example.myvopiserver.infrastructure.custom.jpql.column

import com.example.myvopiserver.domain.*
import org.springframework.stereotype.Component

@Component
class ColumnNames {
    private val qCommentLike = QCommentLike.commentLike
    private val qComment = QComment.comment
    private val qUser = QUser.user
    private val qReply = QReply.reply

    val createdDt = qCommentLike.createdDt.metadata.name
    val updatedDt = qCommentLike.updatedDt.metadata.name
    val status = qCommentLike.status.metadata.name
    val commentId = "${qComment.metadata.name}_${qComment.id.metadata.name}"
    val userId = "${qUser.metadata.name}_${qUser.id.metadata.name}"
    val replyId = "${qReply.metadata.name}_${qReply.id.metadata.name}"
}