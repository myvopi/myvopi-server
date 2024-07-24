package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.enums.CountryCode
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ValidationService(
    private val userReaderStore: UserReaderStore,
) {

    @Transactional(readOnly = true)
    fun validateUserId(userId: String) {
        val user = userReaderStore.findUserByUserId(userId)
        if(user != null) {
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable username")
        }
    }

    @Transactional(readOnly = true)
    fun validateEmail(email: String) {
        val user = userReaderStore.findUserByEmail(email)
        if(user != null) {
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable email")
        }
    }

    fun validateFormatPassword(password: String) {
        val lengthRegex = Regex(".{12,20}") // At least 12 characters
        val specialCharRegex = Regex("[@!#$%&*]") // At least one special character
        val uppercaseRegex = Regex("[A-Z]") // At least one uppercase letter

        if(!lengthRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must be at least 12 characters and maximum of 20 characters")
        if(!specialCharRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one special symbol")
        if(!uppercaseRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one capital letter")
    }

    fun validateCountryCode(countryCode: CountryCode) {
        CountryCode.entries.find { it == countryCode }
            ?: throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad country code request")
    }

}