package com.example.myvopiserver.infrastructure.custom.jpql.repository

import com.example.myvopiserver.domain.command.CommentLikePostRequestCommand
import com.example.myvopiserver.domain.command.ReplyLikePostRequestCommand
import org.springframework.stereotype.Repository

@Repository
interface LikeReaderStoreJpql {

    fun saveCommentLike(command: CommentLikePostRequestCommand)

    fun saveReplyLike(command: ReplyLikePostRequestCommand)
}