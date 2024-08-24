package com.example.adminmyvopiserver.infrastructure.custom.alias

import com.example.adminmyvopiserver.domain.QComment
import com.example.adminmyvopiserver.domain.QReplyLike
import com.example.adminmyvopiserver.domain.QVideo
import com.example.adminmyvopiserver.domain.QUser
import com.example.adminmyvopiserver.domain.QReply
import com.example.adminmyvopiserver.domain.QCommentLike
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

    final val qReplyUser = QUser("ru")
    final val qCommentUser = QUser("cu")
    final val qVideoUser = QUser("vu")
}