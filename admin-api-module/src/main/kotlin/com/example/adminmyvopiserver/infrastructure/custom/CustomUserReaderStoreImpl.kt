package com.example.adminmyvopiserver.infrastructure.custom

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.infrastructure.custom.alias.QEntityAlias
import com.example.adminmyvopiserver.infrastructure.custom.expression.UserExpression
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomUserReaderStore
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CustomUserReaderStoreImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val qEntityAlias: QEntityAlias,
    private val expression: UserExpression,
): CustomUserReaderStore {

    val maxFetchCnt = 10L

    override fun searchUsersRequest(command: UserAdminSearchCommand): List<User> {
        return jpaQueryFactory.selectFrom(qEntityAlias.qUser)
            .where(
                expression.uuid(command.userUuid),
                expression.userName(command.userName),
                expression.userId(command.userId),
                expression.nationality(command.nationality),
                expression.email(command.email),
                expression.status(command.status),
            )
            .orderBy(qEntityAlias.qUser.createdDt.desc())
            .limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }
}