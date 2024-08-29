package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.CommentLikePostRequestCommand
import com.example.myvopiserver.domain.command.ReplyLikePostRequestCommand
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.infrastructure.custom.jpql.repository.LikeReaderStoreJpql
import com.example.myvopiserver.infrastructure.custom.queryDsl.repository.LikeReaderStoreDsl
import com.example.myvopiserver.infrastructure.repository.CommentLikeRepository
import com.example.myvopiserver.infrastructure.repository.ReplyLikeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class LikeReaderStoreImpl(
    private val commentLikeRepository: CommentLikeRepository,
    private val replyLikeRepository: ReplyLikeRepository,
    private val likeReaderStoreDsl: LikeReaderStoreDsl,
    private val likeReaderStoreJpql: LikeReaderStoreJpql,
): LikeReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentLikeDslRequest(commentId: Long, userId: Long): CommentLike? {
        return likeReaderStoreDsl.findCommentLike(commentId, userId)
    }

    @Transactional
    override fun initialSaveCommentLikeJpqlRequest(command: CommentLikePostRequestCommand) {
        likeReaderStoreJpql.saveCommentLike(command)
    }

    @Transactional
    override fun saveCommentLike(commentLike: CommentLike): CommentLike {
        return commentLikeRepository.save(commentLike)
    }

    @Transactional(readOnly = true)
    override fun findReplyLikeDslRequest(replyId: Long, userId: Long): ReplyLike? {
        return likeReaderStoreDsl.findReplyLike(replyId, userId)
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
    override fun initialSaveReplyLikeJpqlRequest(command: ReplyLikePostRequestCommand) {
        likeReaderStoreJpql.saveReplyLike(command)
    }
}