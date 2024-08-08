package com.example.myvopiserver.common.util

import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.enums.VideoType
import com.example.myvopiserver.domain.command.UrlCommand
import org.springframework.stereotype.Component

@Component
class CustomParser {

    private val parseUrlPattern = "^[a-zA-Z]{3}=.*"

    fun validateAndParse(url: String): UrlCommand {
        if(!url.matches(Regex(parseUrlPattern))) throw BadRequestException(ErrorCode.BAD_REQUEST, "Invalid URL format request")
        val videoType = VideoType.decode(url.substring(0, 3))
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        val unparsedVideoId = url.substring(4)
        if(unparsedVideoId.isBlank()) throw NotFoundException(ErrorCode.NOT_FOUND, "Video type not provided")
        return UrlCommand(
            videoType = videoType,
            videoId = if(unparsedVideoId.contains('&')) unparsedVideoId.substringBefore('?') else unparsedVideoId,
        )
    }
}