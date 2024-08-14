package com.entitycoremodule.mapper.comment

import com.entitycoremodule.command.InternalCommentCommand
import com.entitycoremodule.domain.comment.Comment
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
    )
    fun to(
        comment: Comment,
    ): InternalCommentCommand
}