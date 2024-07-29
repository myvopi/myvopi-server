package com.example.myvopiserver.domain.mapper

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.CommentDeleteCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.command.CommentUpdateRequestCommand
import com.example.myvopiserver.domain.command.InternalCommentCommand
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface CommentMapper {

    @Mappings(
        Mapping(source = "filter", target = "filter"),
        Mapping(source = "reqPage", target = "reqPage"),
        Mapping(source = "videoId", target = "videoId"),
        Mapping(source = "videoType", target = "videoType"),
    )
    fun to(
        filter: SearchFilter,
        reqPage: Int,
        videoId: Long,
        videoType: VideoType,
    ): CommentSearchFromVideoCommand

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
        Mapping(source = "command.internalUserInfo", target = "internalUserInfo"),
        Mapping(source = "command.videoId", target = "videoId"),
        Mapping(source = "command.videoType", target = "videoType"),
        Mapping(source = "command.commentUuid", target = "commentUuid"),
        Mapping(source = "status", target = "status"),
    )
    fun deleteTo(
        command: CommentDeleteCommand,
        status: CommentStatus = CommentStatus.DELETED,
    ): CommentUpdateRequestCommand
}