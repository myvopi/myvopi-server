package com.example.myvopiserver.common.config.authentication

import com.commoncoremodule.enums.TokenType
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.example.myvopiserver.common.util.UrlObject
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.service.UserService
import com.example.myvopiserver.domain.service.ValidationService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationManager(
    private val tokenGenerator: JwtTokenGenerator,
    private val userService: UserService,
    private val request: HttpServletRequest,
    urlObject: UrlObject,
    private val validationService: ValidationService,
): AuthenticationManager {

    private val requestMatchers = urlObject.requestMatchers()

    // 어떤 URL이든 인증 정보가 첨부 되었을 시 authentication을 걷친다
    override fun authenticate(authentication: Authentication): Authentication {
        val uri = request.requestURI
        val registeredMethods = requestMatchers[uri] ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val method = request.method
        /*
         * [참고] PostConstruct에서 등록된 URL을 지정/생성 한 뒤 요청에한 URL이 고유한지 판단 {@link com.example.myvopiserver.common.util}
         * [문제] 즉, 이 과정이 필요한 이유는, SpringSecurity에서 requestMatchers중 authenticated()와 permitAll()을 나누더라도
         * permitAll()에 관한 URL요청에 Authroziation header가 담기면 여기를 항상 통과한다, 즉, 어떤 URL이든 authenticated()
         * 과 permitAll()과 관련 없이 Header에 Authroization이 있으면 무조건 filterChain을 걷쳐서 AuthenticationManager까지 도달
         * [해결] 즉, 이것을 해결 할 수 있는 방법이 있다면 좋지만, 지금은 방법을 찾을 수 없으니 임시 우회할 수 있는 방법을 적용
         * 이미 인증을 걷쳐야 할 URL은 패턴중 앞에 /cv (closed version) 를 붙였기 때문에 /cv에 해당 되는 URL은 다 인증을 걷치는 거고
         * 그외에 /cv가 아닌데 여길 통과 했다는 것은 앞단에서 요청을 조작 했다는 의미임
         * 그외에 복합적으로 인증이 필요 하진 않지만, 인증이 첨부 되었을 때 행동 해줘야 할 API들이 있으니, 그것은
         * comment중 [GET] /op/api/v1/comment
         * reply중 [GET] /op/api/v1/reply
         * 임으로 이 두개도 별도의 조건을 추가 해줘야 됨
         * 추가적으로 /cv가 담겨 있지만 requestMatchers에 등록 되지 않는 URL한에서는 authenticationManager를 통과 한 후 NOT_FOUND 발생
         */
        registeredMethods.find { it == method } ?: throw NotFoundException(ErrorCode.NOT_FOUND)

        if(uri.contains("/cv") || uri.equals("/watch") ||
          (method == "GET" && uri.equals("/op/api/v1/comment")) ||
          (method == "GET" && uri.equals("/op/api/v1/reply")))
        {
            val jwt = authentication as BearerTokenAuthenticationToken
            val validatedToken = tokenGenerator.decodeToken(jwt.token)
            val userUuid = tokenGenerator.parseToken(validatedToken, TokenType.ACCESS_TOKEN)
            val internalUserCommand = userService.getUserAndValidateStatus(userUuid)
            // 사용하려는 Token 정보가 Banned인지 확인
            validationService.validateIfBanned(internalUserCommand.status)
            return UsernamePasswordAuthenticationToken(internalUserCommand, "", listOf(SimpleGrantedAuthority(internalUserCommand.role.name)))
        } else throw NotFoundException(ErrorCode.NOT_FOUND)
    }
}

fun Authentication.toUserInfo(): InternalUserCommand {
    return this.principal as InternalUserCommand
}