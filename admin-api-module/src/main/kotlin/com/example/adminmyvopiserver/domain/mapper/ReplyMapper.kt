package com.example.adminmyvopiserver.domain.mapper

import com.example.adminmyvopiserver.domain.Reply
import com.example.adminmyvopiserver.domain.command.InternalReplyCommand
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface ReplyMapper {

    @Mappings(
        Mapping(source = "reply.id", target = "id"),
        Mapping(source = "reply.uuid", target = "uuid"),
        Mapping(source = "reply.content", target = "content"),
        Mapping(source = "reply.modifiedCnt", target = "modifiedCnt"),
        Mapping(source = "reply.status", target = "status"),
        Mapping(source = "userId", target = "userId"),
        Mapping(source = "reply.createdDt", target = "createdDate"),
        Mapping(source = "reply.verificationStatus", target = "verificationStatus"),
    )
    fun to(
        reply: Reply,
        userId: String,
    ): InternalReplyCommand

    @Mappings(
        Mapping(source = "reply.id", target = "id"),
        Mapping(source = "reply.uuid", target = "uuid"),
        Mapping(source = "reply.content", target = "content"),
        Mapping(source = "reply.modifiedCnt", target = "modifiedCnt"),
        Mapping(source = "reply.status", target = "status"),
        Mapping(source = "reply.createdDt", target = "createdDate"),
        Mapping(source = "reply.verificationStatus", target = "verificationStatus"),
    )
    fun to(
        reply: Reply,
    ): InternalReplyCommand
}