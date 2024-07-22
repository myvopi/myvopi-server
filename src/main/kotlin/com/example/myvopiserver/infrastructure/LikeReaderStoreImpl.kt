package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentLikeRepository
import com.example.myvopiserver.infrastructure.repository.ReplyLikeRepository
import org.springframework.stereotype.Repository

@Repository
class LikeReaderStoreImpl(
    private val commentLikeRepository: CommentLikeRepository,
    private val replyLikeRepository: ReplyLikeRepository,
): LikeReaderStore {
}