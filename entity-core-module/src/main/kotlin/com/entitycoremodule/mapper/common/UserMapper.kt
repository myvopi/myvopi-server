package com.entitycoremodule.mapper.common

import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.domain.user.User
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface UserMapper {

    @Mappings(
        Mapping(source = "user.id", target = "id"),
        Mapping(source = "user.userId", target = "userId"),
        Mapping(source = "user.uuid", target = "uuid"),
        Mapping(source = "user.password", target = "password"),
        Mapping(source = "user.name", target = "name"),
        Mapping(source = "user.nationality", target = "nationality"),
        Mapping(source = "user.email", target = "email"),
        Mapping(source = "user.role", target = "role"),
        Mapping(source = "user.status", target = "status"),
    )
    fun to(user: User?): InternalUserCommand?
}