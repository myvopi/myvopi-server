package com.example.adminmyvopiserver.infrastructure.custom

import com.commoncoremodule.enums.CommentStatus
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomReplyReaderStore
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CustomReplyReaderStoreImpl(
    private val em: EntityManager,
): CustomReplyReaderStore {

    override fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand) {
        val query = "UPDATE reply r" +
                    "  JOIN `user` u ON u.id = r.user_id" +
                    "   SET comment_status = '${CommentStatus.DELETED_ADMIN.name}'" +
                    " WHERE r.user_id = ${internalUserCommand.id}" +
                    "   AND u.user_id = '${internalUserCommand.userId}'" +
                    "   AND u.uuid = '${internalUserCommand.uuid}'" +
                    "   AND u.email = '${internalUserCommand.email}'"
        em.createNativeQuery(query).executeUpdate()
    }
}