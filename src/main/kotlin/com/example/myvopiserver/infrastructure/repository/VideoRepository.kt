package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: JpaRepository<Video, Long> {
}