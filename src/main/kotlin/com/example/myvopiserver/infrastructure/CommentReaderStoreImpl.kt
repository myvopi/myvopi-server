package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentRepository
import org.springframework.stereotype.Repository

@Repository
class CommentReaderStoreImpl(
    private val commentRepository: CommentRepository,
): CommentReaderStore {
}