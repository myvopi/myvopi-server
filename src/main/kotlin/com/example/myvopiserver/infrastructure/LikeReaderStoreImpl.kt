package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentLikeReaderStore
import com.example.myvopiserver.infrastructure.repository.CommentLikeRepository
import com.example.myvopiserver.infrastructure.repository.ReplyLikeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class LikeReaderStoreImpl(
    private val commentLikeRepository: CommentLikeRepository,
    private val replyLikeRepository: ReplyLikeRepository,
    private val customCommentLikeReaderStore: CustomCommentLikeReaderStore,
): LikeReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike? {
        return customCommentLikeReaderStore.findCommentLikeRequest(commentId, userId)
    }

    @Transactional(readOnly = true)
    override fun findCommentLike(commentId: Long, userId: Long): CommentLike? {
        return commentLikeRepository.findByIdAndUserId(commentId, userId)
    }

    @Transactional
    override fun saveCommentLikeRequest(command: CommentLikePostCommand) {
        customCommentLikeReaderStore.saveCommentLikeRequest(command)
    }

    @Transactional
    override fun saveCommentLike(commentLike: CommentLike): CommentLike {
        return commentLikeRepository.save(commentLike)
    }

    @Transactional(readOnly = true)
    override fun findCommentLikeByUserAndComment(user: User, comment: Comment): CommentLike? {
        return commentLikeRepository.findByUserAndComment(user, comment)
    }

    @Transactional(readOnly = true)
    override fun findReplyLikeByUserAndReply(user: User, reply: Reply): ReplyLike? {
        return replyLikeRepository.findByUserAndReply(user, reply)
    }

    @Transactional
    override fun saveReplyLike(replyLike: ReplyLike): ReplyLike {
        return replyLikeRepository.save(replyLike)
    }
}