package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Comment
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = ["user", "video"])
    fun findWithUserAndVideoByUuid(uuid: String): Comment?

    @EntityGraph(attributePaths = ["user"])
    fun findWithUserByUuid(uuid: String): Comment?
}