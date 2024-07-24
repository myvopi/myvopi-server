package com.example.myvopiserver.common.config.handler

import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.common.config.exception.*
import com.example.myvopiserver.common.config.response.CommonResponse
import com.example.myvopiserver.common.config.response.CommonResult
import com.example.myvopiserver.common.enums.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): CommonResult<String> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userInfo = authentication.toUserInfo()
        val deniedMsg = if(userInfo.role == MemberRole.ROLE_UNVERIFIED) "Email needs to be verified"
            else ErrorCode.ACCESS_DENIED.engErrorMsg
        return CommonResponse.fail(deniedMsg, ErrorCode.ACCESS_DENIED)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleAccessDeniedException(e: NotFoundException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }
}