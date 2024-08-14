package com.example.myvopiserver.application.video

import com.entitycoremodule.command.CommentSearchFromVideoCommand
import com.entitycoremodule.command.VideoSearchCommand
import com.entitycoremodule.info.VideoBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.VideoService
import org.springframework.stereotype.Service

@Service
class VideoFacade(
    private val videoService: VideoService,
    private val commentService: CommentService,
) {

    fun requestVideoAndComments(
        command: VideoSearchCommand,
    ): VideoBaseInfo
    {
        val returnCommand = videoService.searchVideoOrCreateNewWithReturnMessage(command)
        val searchCommand = CommentSearchFromVideoCommand(
            filter = command.filter,
            reqPage = command.reqPage,
            videoId = returnCommand.internalVideoCommand.id,
            videoType = command.videoType,
            internalUserCommand = command.internalUserCommand,
        )
        val result = commentService.findCommentsFromVideo(searchCommand)
        val commentBaseInfoList = commentService.constructCommentBaseInfo(result)
        return VideoBaseInfo(
            comments = commentBaseInfoList,
            message = returnCommand.message,
        )
    }
}