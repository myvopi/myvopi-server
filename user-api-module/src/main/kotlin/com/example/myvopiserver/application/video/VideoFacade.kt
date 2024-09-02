package com.example.myvopiserver.application.video

import com.example.myvopiserver.domain.command.VideoSearchCommand
import com.example.myvopiserver.domain.info.VideoBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.UserService
import com.example.myvopiserver.domain.service.VideoService
import org.springframework.stereotype.Service

@Service
class VideoFacade(
    private val videoService: VideoService,
    private val commentService: CommentService,
    private val userService: UserService,
) {

    fun requestVideoAndComments(
        command: VideoSearchCommand,
    ): VideoBaseInfo
    {
        userService.validateIfAuthenticationHasBeenApplied(command.internalUserCommand)
        val returnCommand = videoService.searchVideoOrCreateNewWithReturnMessage(command)
        // 주제 생성 시 댓글 조회 생략
        return if(!returnCommand.search) VideoBaseInfo(comments = emptyList(), message = returnCommand.message)
        else {
            val searchCommand = commentService.constructCommentSearchCommandForVideoRequest(command)
            val result = commentService.getCommentsRequest(searchCommand)
            val commentBaseInfoList = commentService.constructCommentBaseInfo(result, command.preferences)
            VideoBaseInfo(comments = commentBaseInfoList, message = returnCommand.message)
        }
    }
}