package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.ReplyReaderStore
import com.example.myvopiserver.infrastructure.repository.ReplyRepository
import org.springframework.stereotype.Repository

@Repository
class ReplyReaderStoreImpl(
    private val replyRepository: ReplyRepository,
): ReplyReaderStore {
}