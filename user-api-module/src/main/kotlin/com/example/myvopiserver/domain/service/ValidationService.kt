package com.example.myvopiserver.domain.service

import com.commoncoremodule.util.Cipher
import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.BaseException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.LikeStatus
import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.domain.user.UserReaderStore
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val userReaderStore: UserReaderStore,
    private val cipher: Cipher,
) {

    fun validateUserIdExists(userId: String) {
        val verification = userReaderStore.userExistsByUserId(userId)
        if(verification) throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable username")
    }

    fun validateEmailExists(email: String) {
        val verification = userReaderStore.userExistsByEmail(email)
        if(verification) throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable email")
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

    fun validateOwnerAndRequester(requesterCommand: InternalUserCommand, entityOwner: User) {
        if(requesterCommand.uuid != entityOwner.uuid || requesterCommand.userId != entityOwner.userId) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Not allowed to request any actions for this comment")
        }
    }

    fun validatePassword(requestPassword: String, password: String) {
        val decryptedPassword = cipher.decrypt(password)
        if(requestPassword != decryptedPassword) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
    }

    fun validateIfRequestContentMatchesOriginalContent(requestContent: String, commentContent: String): Boolean {
        return requestContent == commentContent
    }

    fun validateIsDeleted(status: CommentStatus) {
        if(status == CommentStatus.DELETED) throw BadRequestException(ErrorCode.BAD_REQUEST, "This has already been deleted")
    }

    fun validateIsLiked(status: LikeStatus) {
        if(status == LikeStatus.LIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Already liked")
    }

    fun validateIsUnliked(status: LikeStatus) {
        if(status == LikeStatus.UNLIKED) throw BaseException(ErrorCode.BAD_REQUEST, "Cannot unlike this")
    }
}