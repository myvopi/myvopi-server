package com.example.myvopiserver.common.config.filter

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class CorsFilter : Filter {

    override fun init(filterConfig: FilterConfig) {}

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "*")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Allow-Headers", "*")
        if ("OPTIONS" == request.method) response.status = HttpServletResponse.SC_OK
        else filterChain.doFilter(request, response)
    }
}