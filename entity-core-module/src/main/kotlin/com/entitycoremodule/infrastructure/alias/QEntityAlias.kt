package com.entitycoremodule.infrastructure.alias

import com.entitycoremodule.domain.comment.QComment
import com.entitycoremodule.domain.like.QCommentLike
import com.entitycoremodule.domain.like.QReplyLike
import com.entitycoremodule.domain.reply.QReply
import com.entitycoremodule.domain.user.QUser
import com.entitycoremodule.domain.video.QVideo
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