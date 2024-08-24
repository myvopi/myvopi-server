package com.example.adminmyvopiserver.common.config

import com.example.adminmyvopiserver.common.config.filter.CorsFilter
import com.example.adminmyvopiserver.common.config.filter.JwtAuthenticationFilter
import com.example.adminmyvopiserver.common.config.authentication.JwtAuthenticationManager
import com.example.adminmyvopiserver.common.config.filter.JwtAuthenticationExceptionFilter
import com.example.adminmyvopiserver.common.handler.CustomAccessDeniedHandler
import com.example.adminmyvopiserver.common.handler.CustomAuthenticationEntryPoint
import com.example.adminmyvopiserver.common.handler.CustomUnauthorizedAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.SessionManagementFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val jwtAuthenticationManager: JwtAuthenticationManager,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationExceptionFilter: JwtAuthenticationExceptionFilter,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customUnauthorizedAccessDeniedHandler: CustomUnauthorizedAccessDeniedHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
) {

    @Bean
    fun filterChain(
        http: HttpSecurity
    ): SecurityFilterChain
    {
        http.oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
        http.authenticationManager(jwtAuthenticationManager)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(jwtAuthenticationExceptionFilter, JwtAuthenticationFilter::class.java)
            .exceptionHandling{
                it.accessDeniedHandler(customUnauthorizedAccessDeniedHandler)
            }
        http.httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
        http.cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
        http.sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.addFilterBefore(CorsFilter(), SessionManagementFilter::class.java)
        http.headers { headers ->
            headers.frameOptions { frameOptionsConfig -> frameOptionsConfig.disable() }
        }
        http.headers { headers ->
            headers.xssProtection { xssProtection -> xssProtection.disable() }
        }

        http.authorizeHttpRequests { authorize ->
            authorize.requestMatchers("*", "/admin/api/v1/user/**").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/admin/api/v1/admin/login").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/admin/api/v1/admin/token/re-issue").authenticated()

            authorize.anyRequest().authenticated()
        }
        .exceptionHandling{
            it.accessDeniedHandler(customAccessDeniedHandler)
            it.authenticationEntryPoint(customAuthenticationEntryPoint)
        }
        return http.build()
    }
}