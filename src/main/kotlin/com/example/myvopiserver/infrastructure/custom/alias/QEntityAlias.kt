package com.example.myvopiserver.infrastructure.custom.alias

import com.example.myvopiserver.domain.QComment
import com.example.myvopiserver.domain.QCommentLike
import com.example.myvopiserver.domain.QReply
import com.example.myvopiserver.domain.QReplyLike
import com.example.myvopiserver.domain.QVideo
import com.example.myvopiserver.domain.role.QUser
import org.springframework.stereotype.Component

@Component
class QEntityAlias {
    final val qComment = QComment("c")
    final val qCommentLike = QCommentLike("cl")
    final val qReply2 = QReply("r2")
    final val qUser = QUser("u")
    final val qVideo = QVideo("v")
    final val qComment2 = QComment("c2")
    final val qCommentLike2 = QCommentLike("cl2")
    final val qReply = QReply("r")
    final val qReplyLike = QReplyLike("rl")
    final val qReplyLike2 = QReplyLike("rl2")
}