package com.example.adminmyvopiserver.interfaces.admin

import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.adminmyvopiserver.domain.command.AdminLoginCommand
import com.example.adminmyvopiserver.domain.command.ReissueAccessTokenCommand
import com.example.adminmyvopiserver.domain.info.AuthenticationTokenInfo
import com.example.adminmyvopiserver.application.admin.AdminFacade
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/op/admin/api/v1/admin")
class AdminApiController(
    private val adminFacade: AdminFacade,
) {

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginDto,
    ): CommonResult<AuthenticationTokenInfo>
    {
        val command = AdminLoginCommand(
            userId = body.userId,
            password = body.password,
        )
        val info = adminFacade.loginAdmin(command)
        return CommonResponse.success(info, "Login success")
    }

    @PostMapping("/token/re-issue")
    fun reissueAccessToken(
        @RequestBody body: ReissueAccessTokenDto,
    ): CommonResult<AuthenticationTokenInfo>
    {
        val command = ReissueAccessTokenCommand(
            refreshToken = body.refreshToken,
        )
        val info = adminFacade.reissueAccessToken(command)
        return CommonResponse.success(info, "Reissue success")
    }
}