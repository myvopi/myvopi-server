package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.role.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyLikeRepository: JpaRepository<ReplyLike, Long> {

    fun findByUserAndReply(user: User, reply: Reply): ReplyLike?
}