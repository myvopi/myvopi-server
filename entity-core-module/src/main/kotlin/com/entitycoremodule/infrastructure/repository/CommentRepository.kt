package com.entitycoremodule.infrastructure.repository

import com.entitycoremodule.domain.comment.Comment
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = ["user", "video", "video.user"])
    fun findWithUserAndVideoAndVideoOwnerByUuid(uuid: String): Comment?

    @EntityGraph(attributePaths = ["user"])
    fun findWithUserByUuid(uuid: String): Comment?

    fun findByUuid(uuid: String): Comment?
}