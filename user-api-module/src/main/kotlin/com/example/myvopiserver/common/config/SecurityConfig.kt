package com.example.myvopiserver.common.config

import com.example.myvopiserver.common.config.filter.CorsFilter
import com.example.myvopiserver.common.config.filter.JwtAuthenticationExceptionFilter
import com.example.myvopiserver.common.handler.CustomAccessDeniedHandler
import com.example.myvopiserver.common.handler.CustomAuthenticationEntryPoint
import com.example.myvopiserver.common.handler.CustomUnauthorizedAccessDeniedHandler
import com.example.myvopiserver.common.config.authentication.JwtAuthenticationManager
import com.example.myvopiserver.common.config.filter.JwtAuthenticationFilter
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
        // TODO Velog 같은 곳을 봐도 Authorization 태우는 방식을 알려주지 않을 뿐더러 태우더라도 변조가 되었는지 안되었는지 알려주지 않은체
        // 그냥 200만 계속 내려준다. 서버가 어떤 응답을 해주는지 숨겨야 될 의무가 있는 듯
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
            authorize.requestMatchers(HttpMethod.GET, "/{url}").permitAll()

            authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/email/verification/newCode").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/email/verification").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/token/re-issue").permitAll()

            authorize.requestMatchers(HttpMethod.GET, "/api/v1/comment/**").permitAll()
            authorize.requestMatchers(HttpMethod.PUT, "/api/v1/comment/**").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/comment/**").authenticated()
            authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/comment/**").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/comment/like").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/comment/unlike").authenticated()

            authorize.requestMatchers(HttpMethod.GET, "/api/v1/reply/**").permitAll()
            authorize.requestMatchers(HttpMethod.PUT, "/api/v1/reply/**").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/reply/**").authenticated()
            authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/reply/**").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/reply/like").authenticated()
            authorize.requestMatchers(HttpMethod.POST, "/api/v1/reply/unlike").authenticated()

            authorize.anyRequest().authenticated()
        }
        .exceptionHandling{
            it.accessDeniedHandler(customAccessDeniedHandler)
            it.authenticationEntryPoint(customAuthenticationEntryPoint)
        }
        return http.build()
    }
}