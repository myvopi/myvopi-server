package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Reply
import com.example.adminmyvopiserver.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyRepository: JpaRepository<Reply, Long> {

    @EntityGraph(attributePaths = ["likes", "comment", "comment.video", "user"])
    fun findByUser(user: User, pageable: Pageable): List<Reply>
}