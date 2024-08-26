package com.example.adminmyvopiserver.domain.service

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.extension.toStrings
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.UserReaderStore
import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.ContentsByUserCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.info.CommentBaseJpaInfo
import com.example.adminmyvopiserver.domain.info.ReplyBaseJpaInfo
import com.example.adminmyvopiserver.domain.info.UserContentsInfo
import com.example.adminmyvopiserver.domain.interfaces.CommentReaderStore
import com.example.adminmyvopiserver.domain.interfaces.ReplyReaderStore
import com.example.adminmyvopiserver.domain.mapper.UserMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
    private val commentReaderStore: CommentReaderStore,
    private val replyReaderStore: ReplyReaderStore,
) {

    // Db-transactions (readOnly)
    fun searchUsers(command: UserAdminSearchCommand): List<InternalUserCommand> {
        val results = userReaderStore.searchUsersRequest(command)
        return results.map { userMapper.toForAdmin(it) }
    }

    fun getUserByUserIdAndUuid(userId: String, uuid: String): InternalUserCommand {
        val user = userReaderStore.findUserByUserIdAndUuid(userId, uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No user exists")
        return userMapper.to(user)!!
    }

    fun getContentsByUserAndConvertToInfo(
        internalUserCommand: InternalUserCommand,
        command: ContentsByUserCommand,
    ): List<UserContentsInfo>
    {
        val user = User(command = internalUserCommand)
        val comments = commentReaderStore.findCommentsByUser(user, command.pageable)
            .groupBy { it.video.videoId }
        val replies = replyReaderStore.findRepliesByUser(user, command.pageable)
            .groupBy { it.comment.video.videoId }
        val videos = comments.keys + replies.keys
        return videos.map { videoKey ->
            UserContentsInfo(
                videoId = videoKey,
                comments = comments[videoKey]?.let {
                    it.map { comment ->
                        CommentBaseJpaInfo(
                            uuid = comment.uuid,
                            content = comment.content,
                            modifiedCnt = comment.modifiedCnt,
                            status = comment.status.name,
                            verificationStatus = comment.verificationStatus.name,
                            videoId = videoKey,
                            userId = comment.user.userId,
                            likeCount = comment.likes.count().toLong(),
                            createdDate = comment.createdDt?.toStrings("yyyy-MM-dd HH:mm:ss"),
                        )
                    }
                } ?: run { listOf() },
                replies = replies[videoKey]?.let {
                    it.map { reply ->
                        ReplyBaseJpaInfo(
                            uuid = reply.uuid,
                            content = reply.content,
                            modifiedCnt = reply.modifiedCnt,
                            status = reply.status.name,
                            verificationStatus = reply.verificationStatus.name,
                            videoId = videoKey,
                            userId = reply.user.userId,
                            likeCount = reply.likes.count().toLong(),
                            createdDate = reply.createdDt?.toStrings("yyyy-MM-dd HH:mm:ss"),
                            modified = reply.modifiedCnt,
                        )
                    }
                } ?: run { listOf() },
            )
        }
    }

    // Db-transactions
    fun setUserStatusActive(
        internalUserCommand: InternalUserCommand
    ) {
        val user = User(command = internalUserCommand)
            .apply { setRoleStatusActive() }
        userReaderStore.saveUser(user)
    }

    fun setUserStatusBanned(
        command: UserAdminSetRoleStatusCommand
    ): InternalUserCommand {
        val user = userReaderStore.findUserByUserIdAndUuid(command.userId, command.userUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No user exists")
        user.setRoleStatusBanned()
        val savedUser = userReaderStore.saveUser(user)
        return userMapper.to(savedUser)!!
    }
}