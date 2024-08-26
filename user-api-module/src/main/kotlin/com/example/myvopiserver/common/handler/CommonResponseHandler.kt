package com.example.myvopiserver.common.handler

import com.commoncoremodule.response.CommonResult
import com.example.myvopiserver.common.handler.model.ResponseBodyLog
import com.example.myvopiserver.common.util.extension.getLogger
import io.micrometer.tracing.Tracer
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class CommonResponseHandler(
    private val tracer: Tracer
): ResponseBodyAdvice<Any>
{

    private val logger by getLogger()

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any?
    {
        val traceId = tracer.currentSpan()?.context()?.traceId()
        val spanId = tracer.currentSpan()?.context()?.spanId()

        val log = ResponseBodyLog(
            traceId = traceId,
            spanId = spanId,
            remoteHost = request.remoteAddress.hostString,
            remotePort = request.remoteAddress.port,
            requestURI = request.uri.path.toString(),
            method = request.method.toString(),
            body = body
        )

        logger.info("[SERVER RESPONSE BODY]: ${log.toJsonString()}")

        val returnObject: Any?
        if(body is CommonResult<*>) {
            body.path = request.uri.path.toString()
            body.uniqueCode = "$traceId-$spanId"

            returnObject = body
        } else {
            returnObject = body
        }

        return returnObject
    }
}