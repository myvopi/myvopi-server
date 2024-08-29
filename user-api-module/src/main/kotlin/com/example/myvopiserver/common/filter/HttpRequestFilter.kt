package com.example.myvopiserver.common.filter

import com.example.myvopiserver.common.handler.model.RequestLog
import com.google.gson.Gson
import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import kotlin.collections.HashMap

@Component
class HttpRequestFilter(
    private val tracer: Tracer,
    private val gson: Gson,
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val log = RequestLog(
            traceId = tracer.currentSpan()?.context()?.traceId(),
            spanId = tracer.currentSpan()?.context()?.spanId(),
            remoteHost = request.remoteHost,
            remotePort = request.remotePort,
            requestURI = request.requestURI,
            method = request.method,
            headers = getRequestHeaders(request),
            parameters = getRequestParameters(request),
            body = null
        )
        logger.info("[SERVER REQUEST]: ${gson.toJson(log)}")
        filterChain.doFilter(request, response)
    }

    private fun getRequestHeaders(
        httpRequest: HttpServletRequest
    ): MultiValueMap<String, String>
    {
        val ret = MultiValueMapAdapter(HashMap<String, List<String>>())

        for(headerName in httpRequest.headerNames) {
            if(headerName.lowercase(Locale.getDefault())  == "authorization") {
                ret.add(headerName, "HIDE")
            }
            else ret.add(headerName, httpRequest.getHeader(headerName))
        }
        return ret
    }

    private fun getRequestParameters(
        httpRequest: HttpServletRequest
    ): MultiValueMap<String, String>
    {
        val ret = MultiValueMapAdapter(HashMap<String, List<String>>())

        for(paramName in httpRequest.parameterNames)
        {
            ret.add(paramName, httpRequest.getParameter(paramName))
        }
        return ret
    }
}