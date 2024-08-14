package com.entitycoremodule.command

import com.commoncoremodule.enums.CommentStatus
import java.time.LocalDateTime

data class InternalReplyCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val createdDate: LocalDateTime,
    val userId: String?,
)
