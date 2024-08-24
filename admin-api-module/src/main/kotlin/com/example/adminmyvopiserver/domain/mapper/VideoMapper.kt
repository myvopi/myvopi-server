package com.example.adminmyvopiserver.domain.mapper

import com.example.adminmyvopiserver.domain.Video
import com.example.adminmyvopiserver.domain.command.InternalVideoCommand
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
interface VideoMapper {

    @Mappings(
        Mapping(source = "video.id", target = "id"),
        Mapping(source = "video.uuid", target = "uuid"),
        Mapping(source = "video.videoId", target = "videoId"),
        Mapping(source = "video.user.id", target = "userId"),
        Mapping(source = "video.videoType", target = "videoType"),
    )
    fun to(video: Video): InternalVideoCommand
}