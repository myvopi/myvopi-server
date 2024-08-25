package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: JpaRepository<Video, Long> {
}