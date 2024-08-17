package com.entitycoremodule.domain.interfaces.users

import com.entitycoremodule.domain.report.Report
import com.entitycoremodule.domain.user.User

interface ReportReaderStore {

    fun findCommentReportByContentUuidAndUser(contentUuid: String, user: User): Report?

    fun findReplyReportByContentUuidAndUser(contentUuid: String, user: User): Report?

    fun saveReport(report: Report): Report
}