package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Reply
import com.example.adminmyvopiserver.domain.ReplyLike
import com.example.adminmyvopiserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyLikeRepository: JpaRepository<ReplyLike, Long> {

    fun findByUserAndReply(user: User, reply: Reply): ReplyLike?
}