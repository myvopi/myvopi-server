package com.example.myvopiserver.domain

import com.commoncoremodule.enums.ContentType
import com.commoncoremodule.enums.ReportType
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name ="report")
@DynamicUpdate
@DynamicInsert
class Report(
    contentType: ContentType,   // 콘텐츠 유형
    reportType: ReportType,     // 신고 내용
    targetUuid: String,        // 콘텐츠 UUID
    targetId: Long,            // 콘텐츠 ID
    reporter: User,             // 신고자
    reportTarget: User,         // 신고 대상자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(name = "contentType", nullable = false, updatable = true)
    var contentType: ContentType = contentType
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "reportType", nullable = false, updatable = true)
    var reportType: ReportType = reportType
        protected set

    @Column(name = "targetUuid", nullable = false, updatable = true)
    var targetUuid: String = targetUuid
        protected set

    @Column(name = "targetId", nullable = false, updatable = true)
    var targetId: Long = targetId
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "reporter_id", nullable = false)
    var reporter: User = reporter
        protected set

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User::class,
    )
    @JoinColumn(name = "report_target_id", nullable = false)
    var reportTarget: User = reportTarget
        protected set

    override fun toString(): String {
        return "Report(id=$id, contentType=$contentType, reportType=$reportType, targetUuid='$targetUuid', targetId=$targetId, reporter=$reporter, reportTarget=$reportTarget)"
    }
}