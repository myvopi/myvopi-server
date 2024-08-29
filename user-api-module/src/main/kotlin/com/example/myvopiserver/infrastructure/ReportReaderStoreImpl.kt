package com.example.myvopiserver.infrastructure

import com.commoncoremodule.enums.ContentType
import com.example.myvopiserver.domain.Report
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.interfaces.ReportReaderStore
import com.example.myvopiserver.infrastructure.repository.ReportRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReportReaderStoreImpl(
    private val reportRepository: ReportRepository,
): ReportReaderStore {

    @Transactional(readOnly = true)
    override fun findCommentReportByTargetUuidAndUser(contentUuid: String, user: User): Report? {
        return reportRepository.findByTargetUuidAndContentTypeAndReporter(contentUuid, ContentType.COMMENT, user)
    }

    @Transactional(readOnly = true)
    override fun findReplyReportByTargetUuidAndUser(contentUuid: String, user: User): Report? {
        return reportRepository.findByTargetUuidAndContentTypeAndReporter(contentUuid, ContentType.REPLY, user)
    }

    @Transactional
    override fun saveReport(report: Report): Report {
        return reportRepository.save(report)
    }
}