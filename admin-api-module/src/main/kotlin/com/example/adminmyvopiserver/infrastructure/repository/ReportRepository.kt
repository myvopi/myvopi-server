package com.example.adminmyvopiserver.infrastructure.repository

import com.commoncoremodule.enums.ContentType
import com.example.adminmyvopiserver.domain.Report
import com.example.adminmyvopiserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {

    fun findByContentUuidAndContentTypeAndReporter(uuid: String, type: ContentType, reporter: User): Report?
}