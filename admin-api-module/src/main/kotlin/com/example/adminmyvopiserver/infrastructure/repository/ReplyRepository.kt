package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyRepository: JpaRepository<Reply, Long> {
}