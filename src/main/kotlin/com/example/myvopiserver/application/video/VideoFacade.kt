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
        val internalVideoCommand = videoService.searchVideoOrStore(command)
        val commentSearchCommand = commentMapper.of(
            filter = command.filter,
            reqPage = command.reqPage,
            videoId = internalVideoCommand.id,
        )
        val result = commentService.findComments(commentSearchCommand)
        return commentService.constructCommentBaseInfo(result)
    }
}