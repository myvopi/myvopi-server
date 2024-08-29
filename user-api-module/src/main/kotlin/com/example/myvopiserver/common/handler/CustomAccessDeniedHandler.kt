package com.example.myvopiserver.common.handler

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.response.CommonResponse
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val gson: Gson,
): AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    )
    {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json; charset=UTF-8"

        val exceptionResponse = CommonResponse.fail(
            message = ErrorCode.ACCESS_DENIED.engErrorMsg,
            errorCode = ErrorCode.ACCESS_DENIED
        )
        response.writer.write(gson.toJson(exceptionResponse))
    }
}