package com.example.myvopiserver.interfaces.comment

data class CommentUpdateDto(
    val content: String,
    val commentUuid: String,
    val userId: String,
)

data class CommentPostDto(
    val content: String,
)

data class CommentDeleteDto(
    val commentUuid: String,
    val userId: String,
)
