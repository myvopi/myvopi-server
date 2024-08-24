package com.example.adminmyvopiserver.domain.mapper

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.info.UserInfo
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
    fun toForAdmin(user: User): InternalUserCommand

    @Mappings(
        Mapping(source = "command.id", target = "id"),
        Mapping(source = "command.userId", target = "userId"),
        Mapping(source = "command.uuid", target = "uuid"),
        Mapping(source = "command.password", target = "password"),
        Mapping(source = "command.name", target = "name"),
        Mapping(source = "command.nationality", target = "nationality"),
        Mapping(source = "command.email", target = "email"),
        Mapping(source = "command.role", target = "role"),
        Mapping(source = "command.status", target = "status"),
    )
    fun ofForAdmin(command: InternalUserCommand): UserInfo
}