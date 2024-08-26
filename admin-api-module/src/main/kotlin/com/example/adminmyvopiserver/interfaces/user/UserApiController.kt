package com.example.adminmyvopiserver.interfaces.user

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.RoleStatus
import com.commoncoremodule.response.CommonResponse
import com.commoncoremodule.response.CommonResult
import com.example.adminmyvopiserver.application.user.UserFacade
import com.example.adminmyvopiserver.common.PageableDto
import com.example.adminmyvopiserver.domain.command.ContentsByUserCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.info.UserContentsInfo
import com.example.adminmyvopiserver.domain.info.UserInfo
import org.springframework.data.domain.Sort
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cv/admin/api/v1/user")
class UserApiController(
    private val userFacade: UserFacade,
) {

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    fun searchUsers(
        @RequestParam("userId", required = false) userId: String?,
        @RequestParam("userUuid", required = false) userUuid: String?,
        @RequestParam("userName", required = false) userName: String?,
        @RequestParam("nationality", required = false) nationality: CountryCode?,
        @RequestParam("email", required = false) email: String?,
        @RequestParam("status", required = false) status: RoleStatus?,
        @RequestParam("reqPage", required = true, defaultValue = "0") reqPage: Int,
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
    @PostMapping("/status/active")
    fun setStatusActive(
        @RequestBody body: UserAdminSetRoleStatusDto,
    ): CommonResult<String>
    {
        val command = UserAdminSetRoleStatusCommand(
            userId = body.userId,
            userUuid = body.userUuid,
            email = body.email,
        )
        userFacade.requestUserStatusActive(command)
        return CommonResponse.success("Status changed")
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/status/ban")
    fun setStatusBanned(
        @RequestBody body: UserAdminSetRoleStatusDto,
    ): CommonResult<String>
    {
        val command = UserAdminSetRoleStatusCommand(
            userId = body.userId,
            userUuid = body.userUuid,
            email = body.email,
        )
        userFacade.requestUserStatusBanned(command)
        return CommonResponse.success("Banned user")
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/contents")
    fun getUserContents(
        @RequestBody body: ContentsByUserDto,
    ): CommonResult<List<UserContentsInfo>>
    {
        val command = ContentsByUserCommand(
            userId = body.userId,
            userUuid = body.userUuid,
            email = body.email,
            pageable = PageableDto(
                page = body.reqPage,
                size = 10,
                sorts = listOf(PageableDto.SortDto(Sort.Direction.DESC, "createdDt"))
            ).toPageable(),
        )
        val info = userFacade.requestContentsByUser(command)
        return CommonResponse.success(info)
    }
}