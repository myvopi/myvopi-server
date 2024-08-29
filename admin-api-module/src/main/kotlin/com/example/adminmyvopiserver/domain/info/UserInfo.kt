package com.example.adminmyvopiserver.domain.info

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.enums.RoleStatus

data class UserInfo(
    val id: Long,
    val userId: String,
    val uuid: String,
    val password: String,
    val name: String,
    val nationality: CountryCode,
    val email: String,
    val role: MemberRole,
    val status: RoleStatus,
)

data class UserContentsInfo(
    val videoId: String,
    val comments: List<CommentBaseJpaInfo>,
    val replies: List<ReplyBaseJpaInfo>,
)
