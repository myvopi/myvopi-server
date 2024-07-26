package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.SearchFilter

data class CommentSearchCommand(
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: Long,
    // TODO user id
)