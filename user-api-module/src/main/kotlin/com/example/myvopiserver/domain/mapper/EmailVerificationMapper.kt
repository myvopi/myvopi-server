package com.example.myvopiserver.domain.mapper

import com.example.myvopiserver.domain.EmailVerification
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.EmailVerificationCommand
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