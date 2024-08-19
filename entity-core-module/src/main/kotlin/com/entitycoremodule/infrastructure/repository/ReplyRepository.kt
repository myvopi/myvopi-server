package com.entitycoremodule.infrastructure.repository

import com.entitycoremodule.domain.reply.Reply
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyRepository: JpaRepository<Reply, Long> {

    @EntityGraph(attributePaths = ["user"])
    fun findWithUserByUuid(uuid: String): Reply?

    fun findByUuid(uuid: String): Reply?
}