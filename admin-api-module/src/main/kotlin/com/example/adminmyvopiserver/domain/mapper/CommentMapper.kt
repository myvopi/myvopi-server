package com.example.adminmyvopiserver.domain.mapper

import com.example.adminmyvopiserver.domain.Comment
import com.example.adminmyvopiserver.domain.command.InternalCommentCommand
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface CommentMapper {

    @Mappings(
        Mapping(source = "comment.id", target = "id"),
        Mapping(source = "comment.uuid", target = "uuid"),
        Mapping(source = "comment.content", target = "content"),
        Mapping(source = "comment.modifiedCnt", target = "modifiedCnt"),
        Mapping(source = "comment.status", target = "status"),
        Mapping(source = "userId", target = "userId"),
        Mapping(source = "comment.createdDt", target = "createdDate"),
        Mapping(source = "comment.verificationStatus", target = "verificationStatus"),
    )
    fun to(
        comment: Comment,
        userId: String,
    ): InternalCommentCommand

    @Mappings(
        Mapping(source = "comment.id", target = "id"),
        Mapping(source = "comment.uuid", target = "uuid"),
        Mapping(source = "comment.content", target = "content"),
        Mapping(source = "comment.modifiedCnt", target = "modifiedCnt"),
        Mapping(source = "comment.status", target = "status"),
        Mapping(source = "comment.createdDt", target = "createdDate"),
        Mapping(source = "comment.verificationStatus", target = "verificationStatus"),
    )
    fun to(
        comment: Comment,
    ): InternalCommentCommand
}