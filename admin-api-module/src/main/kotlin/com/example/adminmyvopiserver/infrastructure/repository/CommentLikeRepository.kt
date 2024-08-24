package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.CommentLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository: JpaRepository<CommentLike, Long> {
}