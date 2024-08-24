package com.example.adminmyvopiserver.domain.mapper

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.EmailVerification
import com.example.adminmyvopiserver.domain.command.EmailVerificationCommand
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