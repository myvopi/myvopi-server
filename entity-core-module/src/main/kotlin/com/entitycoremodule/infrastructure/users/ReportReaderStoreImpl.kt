package com.entitycoremodule.infrastructure.users

import com.commoncoremodule.enums.ContentType
import com.entitycoremodule.domain.interfaces.users.ReportReaderStore
import com.entitycoremodule.domain.report.Report
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.infrastructure.repository.ReportRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReportReaderStoreImpl(
    private val reportRepository: ReportRepository,
): ReportReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentReportByContentUuidAndUser(contentUuid: String, user: User): Report? {
        return reportRepository.findByContentUuidAndContentTypeAndReporter(contentUuid, ContentType.COMMENT, user)
    }

    @Transactional(readOnly = true)
    override fun findReplyReportByContentUuidAndUser(contentUuid: String, user: User): Report? {
        return reportRepository.findByContentUuidAndContentTypeAndReporter(contentUuid, ContentType.REPLY, user)
    }

    @Transactional
    override fun saveReport(report: Report): Report {
        return reportRepository.save(report)
    }
}