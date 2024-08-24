package com.example.adminmyvopiserver.interfaces.user

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.RoleStatus
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.adminmyvopiserver.application.user.UserFacade
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.info.UserInfo
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/v1/user")
class UserApiController(
    private val userFacade: UserFacade,
) {

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    fun searchUsers(
        authentication: Authentication,
        @RequestParam("userId", required = false) userId: String?,
        @RequestParam("userUuid", required = false) userUuid: String?,
        @RequestParam("userName", required = false) userName: String?,
        @RequestParam("nationality", required = false) nationality: CountryCode?,
        @RequestParam("email", required = false) email: String?,
        @RequestParam("status", required = false) status: RoleStatus?,
        @RequestParam("reqPage", required = false) reqPage: Int,
    ): CommonResult<List<UserInfo>>
    {
        val command = UserAdminSearchCommand(
            userId = userId,
            userUuid = userUuid,
            userName = userName,
            nationality = nationality,
            email = email,
            status = status,
            reqPage = reqPage,
        )
        val info = userFacade.requestUsers(command)
        return CommonResponse.success(info)
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/status")
    fun setStatus(
        authentication: Authentication,
        @RequestBody body: UserAdminSetRoleStatusDto,
    ): CommonResult<String>
    {
        val command = UserAdminSetRoleStatusCommand(
            userId = body.userId,
            userUuid = body.userUuid,
            email = body.email,
            status = body.status,
        )
        userFacade.requestUserStatus(command)
        return CommonResponse.success("Status changed")
    }
}