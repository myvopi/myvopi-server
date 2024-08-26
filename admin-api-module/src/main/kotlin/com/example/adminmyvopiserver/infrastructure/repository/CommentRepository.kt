package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Comment
import com.example.adminmyvopiserver.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    // TODO pageable
    fun findByCreatedDtBetween(fromDate: LocalDateTime, toDate: LocalDateTime): List<Comment>

    @EntityGraph(attributePaths = ["likes", "video", "user"])
    fun findByUser(user: User, pageable: Pageable): List<Comment>
}