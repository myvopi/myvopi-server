package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findCommentsFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple>

    fun findCommentsFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple>
}