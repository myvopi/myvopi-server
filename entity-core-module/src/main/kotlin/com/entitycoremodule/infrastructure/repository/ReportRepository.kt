package com.entitycoremodule.infrastructure.repository

import com.commoncoremodule.enums.ContentType
import com.entitycoremodule.domain.report.Report
import com.entitycoremodule.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {

    fun findByContentUuidAndContentTypeAndReporter(uuid: String, type: ContentType, reporter: User): Report?
}