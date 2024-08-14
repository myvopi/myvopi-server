package com.example.myvopiserver.interfaces.user

import com.entitycoremodule.command.EmailVerifyReqCommand
import com.example.myvopiserver.application.user.UserFacade
import com.authcoremodule.authentication.toUserInfo
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.entitycoremodule.command.ReissueAccessTokenCommand
import com.entitycoremodule.command.UserLoginCommand
import com.entitycoremodule.command.UserRegisterCommand
import com.entitycoremodule.info.AuthenticationTokenInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserApiController(
    private val userFacade: UserFacade,
) {

    @PostMapping("/register")
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

    @PostMapping("/login")
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
    @PostMapping("/email/verification/newCode")
    fun emailVerificationNewCode(
        authentication: Authentication,
    ): CommonResult<String>
    {
        val command = authentication.toUserInfo()
        userFacade.requestEmailVerificationCode(command)
        return CommonResponse.success("Verification email sent")
    }

    @Secured("ROLE_UNVERIFIED")
    @PostMapping("/email/verification")
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

    @PostMapping("/token/re-issue")
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