package com.example.myvopiserver.common.filter

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.JwtException
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.response.CommonResponse
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.JwtValidationException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ExceptionFilter(
    private val gson: Gson,
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    )
    {
        try{
            filterChain.doFilter(request, response)
        } catch (e: JwtValidationException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ErrorCode.EXPIRED_TOKEN)
        } catch (e: BadJwtException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ErrorCode.JWT_CORRUPT)
        } catch (e: NullPointerException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ErrorCode.UNAUTHORIZED)
        } catch (e: JwtException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ErrorCode.JWT_CORRUPT)
        } catch (e: UnauthorizedException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ErrorCode.UNAUTHORIZED)
        } catch (e: NotFoundException) {
            setErrorResponse(HttpStatus.NOT_FOUND, request, response, ErrorCode.NOT_FOUND)
        } catch (e: AccessDeniedException) {
            setErrorResponse(HttpStatus.FORBIDDEN, request, response, ErrorCode.ACCESS_DENIED)
        }
    }

    private fun setErrorResponse(
        httpStatus: HttpStatus,
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: ErrorCode,
    )
    {
        response.status = httpStatus.value()
        response.contentType = "application/json; charset=UTF-8"
        val exceptionResponse = CommonResponse.fail(e)
        exceptionResponse.path = request.requestURI
        logger.info("[SERVER RESPONSE]: $exceptionResponse")
        response.writer.write(gson.toJson(CommonResponse.fail(e)))
    }
}