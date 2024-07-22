package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.CommentLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository: JpaRepository<CommentLike, Long> {
}