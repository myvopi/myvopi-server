package com.example.myvopiserver.domain.mapper

import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.role.User
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
    )
    fun of(user: User?): InternalUserCommand
}