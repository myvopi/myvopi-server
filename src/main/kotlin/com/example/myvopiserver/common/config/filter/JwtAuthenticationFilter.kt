package com.example.myvopiserver.common.config.filter

import com.example.myvopiserver.common.config.authentication.JwtTokenGenerator
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenGenerator: JwtTokenGenerator,
) : OncePerRequestFilter() {

    private val headerString = "Authorization"
    private val tokenPrefix = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(headerString)
        if(request.requestURI.startsWith("/api") && !header.isNullOrBlank() && header.startsWith(tokenPrefix)) {
            val token = header.replace(tokenPrefix, "")
            jwtTokenGenerator.parseTokenFilter(token)
        }
        filterChain.doFilter(request, response)
    }
}