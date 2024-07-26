package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.command.CommentSearchCommand
import com.querydsl.core.Tuple

interface CommentReaderStore {

    fun findComments(command: CommentSearchCommand): List<Tuple>
}