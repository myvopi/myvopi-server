package com.authcoremodule.filter

import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.response.CommonResponse
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationExceptionFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: UnauthorizedException) {
            setErrorResponse(response, e)
        }
    }

    private fun setErrorResponse(response: HttpServletResponse, e: UnauthorizedException) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json; charset=UTF-8"
        val exceptionResponse = CommonResponse.fail(e.errorCode, e.errorCode.name)
        response.writer.write(Gson().toJson(exceptionResponse))
    }
}