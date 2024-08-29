package com.example.myvopiserver.interfaces.dto.comment

data class CommentUpdateDto(
    val content: String,
    val uuid: String,
    val videoType: String,
    val videoId: String,
)

data class CommentPostDto(
    val content: String,
    val videoType: String,
    val videoId: String,
)

data class CommentDeleteDto(
    val uuid: String,
    val videoType: String,
    val videoId: String,
)

data class CommentLikeDto(
    val uuid: String,
    val videoType: String,
    val videoId: String,
)

data class CommentReportDto(
    val uuid: String,
    val reportType: String,
)
