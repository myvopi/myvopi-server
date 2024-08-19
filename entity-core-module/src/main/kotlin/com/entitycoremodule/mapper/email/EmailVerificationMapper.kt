package com.entitycoremodule.mapper.email

import com.entitycoremodule.command.EmailVerificationCommand
import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.user.User
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface EmailVerificationMapper {

    @Mappings(
        Mapping(source = "user.id", target = "id"),
        Mapping(source = "user.userId", target = "userId"),
        Mapping(source = "user.email", target = "email"),
        Mapping(source = "emailVerification.code", target = "code"),
    )
    fun to(user: User, emailVerification: EmailVerification): EmailVerificationCommand
}