package com.example.adminmyvopiserver.common.config.filter

import com.example.adminmyvopiserver.common.config.authentication.JwtAuthenticationManager
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtAuthenticationManager: JwtAuthenticationManager,
) : OncePerRequestFilter() {

    private val headerString = "Authorization"
    private val tokenPrefix = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(headerString)
        val uriStartWith = request.requestURI.contains("/cv")
        if(uriStartWith && !header.isNullOrBlank() && header.startsWith(tokenPrefix)) {
            val token = header.replace(tokenPrefix, "")
            val authentication = jwtAuthenticationManager.authenticate(token)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}