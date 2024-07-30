package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import com.example.myvopiserver.common.enums.CountryCode
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.role.User
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val userReaderStore: UserReaderStore,
) {

    fun validateUserIdExists(userId: String) {
        val user = userReaderStore.findUserByUserId(userId)
        if(user != null) {
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable username")
        }
    }

    fun validateEmailExists(email: String) {
        val user = userReaderStore.findUserByEmail(email)
        if(user != null) {
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable email")
        }
    }

    fun validatePasswordFormat(password: String) {
        val lengthRegex = Regex(".{12,20}") // At least 12 characters
        val specialCharRegex = Regex("[@!#$%&*]") // At least one special character
        val uppercaseRegex = Regex("[A-Z]") // At least one uppercase letter

        if(!lengthRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must be at least 12 characters and maximum of 20 characters")
        if(!specialCharRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one special symbol")
        if(!uppercaseRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one capital letter")
    }

    fun validateValidCountryCode(countryCode: CountryCode) {
        CountryCode.entries.find { it == countryCode }
            ?: throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad country code request")
    }

    fun validateOwnerAndRequester(
        user: User,
        command: InternalUserCommand
    ) {
        if(command.uuid != user.uuid || command.userId != user.userId) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Not allowed to request any actions for this comment")
        }
    }
}