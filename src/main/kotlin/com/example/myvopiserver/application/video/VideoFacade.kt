package com.example.myvopiserver.application.video

import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.VideoService
import org.springframework.stereotype.Service

@Service
class VideoFacade(
    private val videoService: VideoService,
    private val commentService: CommentService,
    private val commentMapper: CommentMapper,
) {

    fun requestVideoAndComments(
        command: VideoSearchCommand,
    ): List<CommentBaseInfo>
    {
        val internalVideoCommand = videoService.searchVideoOrCreateNew(command)
        val searchCommand = commentMapper.to(
            filter = command.filter,
            reqPage = command.reqPage,
            videoId = internalVideoCommand.id,
            videoType = command.videoType,
        )
        val result = commentService.findCommentsFromVideo(searchCommand)
        return commentService.constructCommentBaseInfo(result)
    }
}