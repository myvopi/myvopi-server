package com.example.adminmyvopiserver.infrastructure.custom.expression

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.RoleStatus
import com.example.adminmyvopiserver.infrastructure.custom.alias.QEntityAlias
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.stereotype.Component

@Component
class UserExpression(
    private val qEntityAlias: QEntityAlias,
) {

    fun id(id: Long?): BooleanExpression? {
        return id?.let { qEntityAlias.qUser.id.eq(id) }
    }

    fun uuid(uuid: String?): BooleanExpression? {
        return uuid?.let { qEntityAlias.qUser.uuid.eq(uuid) }
    }

    fun userName(userName: String?): BooleanExpression? {
        return userName?.let { qEntityAlias.qUser.name.eq(userName) }
    }

    fun userId(userId: String?): BooleanExpression? {
        return userId?.let { qEntityAlias.qUser.userId.eq(userId) }
    }

    fun nationality(nationality: CountryCode?): BooleanExpression? {
        return nationality?.let { qEntityAlias.qUser.nationality.eq(nationality) }
    }

    fun email(email: String?): BooleanExpression? {
        return email?.let { qEntityAlias.qUser.email.eq(email) }
    }

    fun status(status: RoleStatus?): BooleanExpression? {
        return status?.let { qEntityAlias.qUser.status.eq(status) }
    }
}