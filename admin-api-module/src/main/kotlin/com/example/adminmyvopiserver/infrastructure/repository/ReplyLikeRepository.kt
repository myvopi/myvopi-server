package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.ReplyLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyLikeRepository: JpaRepository<ReplyLike, Long> {
}