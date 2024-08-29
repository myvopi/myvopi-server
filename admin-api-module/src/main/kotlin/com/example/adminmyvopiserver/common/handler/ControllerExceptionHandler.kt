package com.example.adminmyvopiserver.common.handler

import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.commoncoremodule.exception.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
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
        return CommonResponse.fail(ErrorCode.ACCESS_DENIED.engErrorMsg, ErrorCode.ACCESS_DENIED)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleAccessDeniedException(e: NotFoundException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalException::class)
    fun handleInternalException(e: InternalException): CommonResult<String> {
        return CommonResponse.fail(e.message, e.errorCode)
    }
}