package com.entitycoremodule.infrastructure.repository

import com.entitycoremodule.domain.like.ReplyLike
import com.entitycoremodule.domain.reply.Reply
import com.entitycoremodule.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyLikeRepository: JpaRepository<ReplyLike, Long> {

    fun findByUserAndReply(user: User, reply: Reply): ReplyLike?
}