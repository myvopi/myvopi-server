package com.example.myvopiserver.interfaces.dto.reply

data class ReplyUpdateDto (
    val content: String,
    val uuid: String,
)

data class ReplyPostDto(
    val content: String,
    val commentUuid: String,
)

data class ReplyDeleteDto(
    val uuid: String,
)

data class ReplyLikeDto(
    val uuid: String,
)

data class ReplyReportDto(
    val uuid: String,
    val reportType: String,
)
