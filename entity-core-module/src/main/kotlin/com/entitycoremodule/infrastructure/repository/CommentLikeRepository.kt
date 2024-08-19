package com.entitycoremodule.infrastructure.repository

import com.entitycoremodule.domain.like.CommentLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository: JpaRepository<CommentLike, Long> {
}