package com.example.myvopiserver.domain.mapper

import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
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
    )
    fun of(
        filter: SearchFilter,
        reqPage: Int,
        videoId: Long,
    ): CommentSearchFromVideoCommand
}