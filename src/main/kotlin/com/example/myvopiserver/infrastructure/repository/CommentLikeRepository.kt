package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.role.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository: JpaRepository<CommentLike, Long> {

    fun findByUserAndComment(user: User, comment: Comment): CommentLike?
}