package com.example.myvopiserver.infrastructure.custom.jpql.repository

import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand
import org.springframework.stereotype.Repository

@Repository
interface LikeReaderStoreJpql {

    fun saveCommentLike(command: CommentLikePostCommand)

    fun saveReplyLike(command: ReplyLikePostCommand)
}