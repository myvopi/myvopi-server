package com.example.myvopiserver.common.config

import com.example.myvopiserver.common.config.authentication.JwtAuthenticationManager
import com.example.myvopiserver.common.filter.*
import com.example.myvopiserver.common.handler.CustomAccessDeniedHandler
import com.example.myvopiserver.common.handler.CustomAuthenticationEntryPoint
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val jwtAuthenticationManager: JwtAuthenticationManager,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val httpRequestFilter: HttpRequestFilter,
    private val exceptionFilter: ExceptionFilter,
) {

    @Bean
    fun filterChain(
        http: HttpSecurity
    ): SecurityFilterChain
    {
        http.oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
        http.authenticationManager(jwtAuthenticationManager)
        http.addFilterBefore(exceptionFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(httpRequestFilter, ExceptionFilter::class.java)

        http.httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
        http.cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
        http.sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(CorsFilter(), HttpRequestFilter::class.java)

        http.headers { headers ->
            headers.frameOptions { frameOptionsConfig -> frameOptionsConfig.disable() }
        }
        http.headers { headers ->
            headers.xssProtection { xssProtection -> xssProtection.disable() }
        }

        http.authorizeHttpRequests { authorize ->
            authorize.requestMatchers(HttpMethod.GET, "/watch").permitAll()
            authorize.requestMatchers("*", "/op/**").permitAll()
            authorize.requestMatchers("*", "/cv/**").authenticated()
        }
        .exceptionHandling{
            it.accessDeniedHandler(customAccessDeniedHandler)
            it.authenticationEntryPoint(customAuthenticationEntryPoint)
        }
        return http.build()
    }
}