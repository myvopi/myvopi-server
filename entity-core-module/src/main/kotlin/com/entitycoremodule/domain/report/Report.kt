package com.entitycoremodule.domain.report

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.ContentType
import com.entitycoremodule.domain.BaseTime
import com.entitycoremodule.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name ="report")
class Report(
    contentType: ContentType,   // 콘텐츠 유형
    contentUuid: String,        // 콘텐츠 UUID
    contentId: Long,            // 콘텐츠 ID
    reporter: User,             // 신고자
    reportTarget: User,         // 신고 대상자
): BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, updatable = true)
    var contentType: ContentType = contentType
        protected set



    @Column(name = "content_uuid", nullable = false, updatable = true)
    var contentUuid: String = contentUuid
        protected set

    @Column(name = "content_id", nullable = false, updatable = true)
    var contentId: Long = contentId
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
        return "Report(id=$id, contentType=$contentType, contentUuid='$contentUuid', contentId=$contentId)"
    }
}