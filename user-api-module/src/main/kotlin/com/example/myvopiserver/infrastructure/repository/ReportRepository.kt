package com.example.myvopiserver.infrastructure.repository

import com.commoncoremodule.enums.ContentType
import com.example.myvopiserver.domain.Report
import com.example.myvopiserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {

    fun findByTargetUuidAndContentTypeAndReporter(uuid: String, type: ContentType, reporter: User): Report?
}