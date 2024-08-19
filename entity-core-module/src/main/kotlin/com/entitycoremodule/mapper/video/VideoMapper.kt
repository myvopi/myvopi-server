package com.entitycoremodule.mapper.video

import com.entitycoremodule.command.InternalVideoCommand
import com.entitycoremodule.domain.video.Video
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