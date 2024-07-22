package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyRepository: JpaRepository<Reply, Long> {
}