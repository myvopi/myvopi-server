package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Report
import com.example.myvopiserver.domain.User

interface ReportReaderStore {

    fun findCommentReportByContentUuidAndUser(contentUuid: String, user: User): Report?

    fun findReplyReportByContentUuidAndUser(contentUuid: String, user: User): Report?

    fun saveReport(report: Report): Report
}