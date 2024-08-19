package com.example.myvopiserver.interfaces.comment

data class CommentUpdateDto(
    val content: String,
    val uuid: String,
)

data class CommentPostDto(
    val content: String,
)

data class CommentDeleteDto(
    val uuid: String,
)

data class CommentLikeDto(
    val uuid: String,
)

data class CommentReportDto(
    val uuid: String,
    val reportType: String,
)
