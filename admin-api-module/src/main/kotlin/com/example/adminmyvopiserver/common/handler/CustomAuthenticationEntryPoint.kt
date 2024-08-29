package com.example.adminmyvopiserver.common.handler

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.response.CommonResponse
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        response.status = HttpServletResponse.SC_NOT_FOUND
        response.contentType = "application/json; charset=UTF-8"

        val exceptionResponse = CommonResponse.fail(
            message = ErrorCode.PAGE_NOT_FOUND.engErrorMsg,
            errorCode = ErrorCode.PAGE_NOT_FOUND
        )
        response.writer.write(Gson().toJson(exceptionResponse))
    }
}