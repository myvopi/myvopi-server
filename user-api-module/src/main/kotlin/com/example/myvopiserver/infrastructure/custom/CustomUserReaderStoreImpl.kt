package com.example.myvopiserver.infrastructure.custom

import com.example.myvopiserver.domain.QUser
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import com.example.myvopiserver.infrastructure.custom.repository.CustomUserReaderStore
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CustomUserReaderStoreImpl(
    private val jpaQueryFactory: JPAQueryFactory,
): CustomUserReaderStore {

    private val path = PathBuilder(User::class.java, QUser.user.metadata.name)

    override fun updateUserRequest(command: InternalUserCommand, commandList: List<UpdateClauseCommand>) {
        val qUser = QUser.user
        val updateClause = jpaQueryFactory.update(qUser)
            .where(
                qUser.id.eq(command.id),
                qUser.userId.eq(command.userId),
                qUser.uuid.eq(command.uuid),
                qUser.email.eq(command.email),
            )
        commandList.forEach { updateCommand ->
            updateClause.set(path.get(updateCommand.pathName), updateCommand.value)
        }
        updateClause.execute()
    }
}