package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.infrastructure.custom.repository.CustomLikeReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentLikeRepository
import com.example.myvopiserver.infrastructure.repository.ReplyLikeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class LikeReaderStoreImpl(
    private val commentLikeRepository: CommentLikeRepository,
    private val replyLikeRepository: ReplyLikeRepository,
    private val customLikeReaderStore: CustomLikeReaderStore,
): LikeReaderStore {

    // Comment like
    @Transactional(readOnly = true)
    override fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike? {
        return customLikeReaderStore.findCommentLikeRequest(commentId, userId)
    }

    @Transactional
    override fun initialSaveCommentLikeRequest(command: CommentLikePostCommand) {
        customLikeReaderStore.saveCommentLikeRequest(command)
    }

    @Transactional
    override fun saveCommentLike(commentLike: CommentLike): CommentLike {
        return commentLikeRepository.save(commentLike)
    }

    // Reply like
    @Transactional(readOnly = true)
    override fun findReplyLikeRequest(replyId: Long, userId: Long): ReplyLike? {
        return customLikeReaderStore.findReplyLikeRequest(replyId, userId)
    }

    @Transactional(readOnly = true)
    override fun findReplyLikeByUserAndReply(user: User, reply: Reply): ReplyLike? {
        return replyLikeRepository.findByUserAndReply(user, reply)
    }

    @Transactional
    override fun saveReplyLike(replyLike: ReplyLike): ReplyLike {
        return replyLikeRepository.save(replyLike)
    }

    @Transactional
    override fun initialSaveReplyLikeRequest(command: ReplyLikePostCommand) {
        customLikeReaderStore.saveReplyLikeRequest(command)
    }
}