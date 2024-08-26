package com.example.myvopiserver.interfaces.user

import com.example.myvopiserver.application.user.UserFacade
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.myvopiserver.common.config.authentication.toUserInfo
import com.example.myvopiserver.domain.command.EmailVerifyReqCommand
import com.example.myvopiserver.domain.command.ReissueAccessTokenCommand
import com.example.myvopiserver.domain.command.UserLoginCommand
import com.example.myvopiserver.domain.command.UserRegisterCommand
import com.example.myvopiserver.domain.info.AuthenticationTokenInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApiController(
    private val userFacade: UserFacade,
) {

    @PostMapping("/op/api/v1/user/register")
    fun register(
        @RequestBody body: RegisterDto,
    ): CommonResult<String>
    {
        val command = UserRegisterCommand(
            name = body.name,
            userId = body.userId,
            nationality = body.nationality,
            password = body.password,
            email = body.email,
        )
        userFacade.registerUser(command)
        return CommonResponse.success("Register success")
    }

    @PostMapping("/op/api/v1/user/login")
    fun login(
        @RequestBody body: LoginDto,
    ): CommonResult<AuthenticationTokenInfo>
    {
        val command = UserLoginCommand(
            userId = body.userId,
            password = body.password,
        )
        val info = userFacade.loginUser(command)
        return CommonResponse.success(info, "Login success")
    }

    @Secured("ROLE_UNVERIFIED")
    @PostMapping("/cv/api/v1/user/email/verification/newCode")
    fun emailVerificationNewCode(
        authentication: Authentication,
    ): CommonResult<String>
    {
        val command = authentication.toUserInfo()
        userFacade.requestEmailVerificationCode(command)
        return CommonResponse.success("Verification email sent")
    }

    @Secured("ROLE_UNVERIFIED")
    @PostMapping("/cv/api/v1/user/email/verification")
    fun emailVerification(
        authentication: Authentication,
        @RequestBody body: EmailVerificationDto,
    ): CommonResult<String>
    {
        val internalUserCommand = authentication.toUserInfo()
        val command = EmailVerifyReqCommand(
            internalUserCommand = internalUserCommand,
            reqCode = body.code,
        )
        userFacade.verifyEmail(command)
        return CommonResponse.success("Verification successful")
    }

    @PostMapping("/op/api/v1/user/token/re-issue")
    fun reissueAccessToken(
        @RequestBody body: ReissueAccessTokenDto,
    ): CommonResult<AuthenticationTokenInfo>
    {
        val command = ReissueAccessTokenCommand(
            refreshToken = body.refreshToken,
        )
        val info = userFacade.reissueAccessToken(command)
        return CommonResponse.success(info, "Reissue success")
    }
}