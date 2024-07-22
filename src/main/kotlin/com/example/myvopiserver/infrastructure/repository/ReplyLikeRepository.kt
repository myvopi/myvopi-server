package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.ReplyLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyLikeRepository: JpaRepository<ReplyLike, Long> {
}