package com.example.myvopiserver.infrastructure.custom.queryDsl

import com.example.myvopiserver.domain.QUser
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import com.example.myvopiserver.infrastructure.custom.queryDsl.repository.UserReaderStoreDsl
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserReaderStoreDslImpl(
    private val em: EntityManager,
): UserReaderStoreDsl {

    private val path = PathBuilder(User::class.java, QUser.user.metadata.name)
    private val qUser = QUser.user

    override fun updateUser(command: InternalUserCommand, commandList: List<UpdateClauseCommand>) {
        val updateClause = JPAQueryFactory(em)
            .update(qUser)
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