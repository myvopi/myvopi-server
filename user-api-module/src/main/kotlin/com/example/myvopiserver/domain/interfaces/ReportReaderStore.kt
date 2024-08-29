package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Report
import com.example.myvopiserver.domain.User

interface ReportReaderStore {

    fun findCommentReportByTargetUuidAndUser(contentUuid: String, user: User): Report?

    fun findReplyReportByTargetUuidAndUser(contentUuid: String, user: User): Report?

    fun saveReport(report: Report): Report
}