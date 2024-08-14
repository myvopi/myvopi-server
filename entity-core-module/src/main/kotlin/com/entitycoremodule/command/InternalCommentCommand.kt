package com.entitycoremodule.command

import com.commoncoremodule.enums.CommentStatus
import java.time.LocalDateTime

data class InternalCommentCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val userId: String?,
    val createdDate: LocalDateTime,
)
