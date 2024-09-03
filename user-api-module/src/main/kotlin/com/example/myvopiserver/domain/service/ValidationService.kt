package com.example.myvopiserver.domain.service

import com.commoncoremodule.enums.*
import com.commoncoremodule.util.Cipher
import com.commoncoremodule.exception.*
import com.commoncoremodule.extension.getCurrentDateTime
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class ValidationService(
    private val userReaderStore: UserReaderStore,
    private val cipher: Cipher,
) {

    private val emailPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".toRegex()

    private val passwordLengthRegex = Regex(".{12,20}") // At least 12 characters
    private val passwordSpecialCharRegex = Regex("[@!#$%&*]") // At least one special character
    private val passwordUppercaseRegex = Regex("[A-Z]") // At least one uppercase letter

    fun validateEmail(email: String) {
        val verification = userReaderStore.userExistsByEmail(email)
        if(verification) throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable email")
    }

    fun validateUserIdOrEmailExists(userId: String, email: String) {
        val verification = userReaderStore.userExistsByUserIdOrEmail(userId, email)
        if(verification) throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable username or email")
    }

    fun validateEmailFormat(email: String) {
        if(!emailPattern.matches(email)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Unavailable email format")
    }

    fun validatePasswordFormat(password: String) {
        if(!passwordLengthRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must be at least 12 characters and maximum of 20 characters")
        if(!passwordSpecialCharRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one special symbol")
        if(!passwordUppercaseRegex.containsMatchIn(password)) throw BadRequestException(ErrorCode.BAD_REQUEST, "Password must contain at least one capital letter")
    }

    fun validateValidCountryCode(countryCode: CountryCode) {
        CountryCode.entries.find { it == countryCode }
            ?: throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad country code request")
    }

    fun validateOwnerAndRequester(requesterCommand: InternalUserCommand, entityOwner: User) {
        if(requesterCommand.uuid != entityOwner.uuid || requesterCommand.email != entityOwner.email) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Not allowed to request any actions for this comment")
        }
    }

    fun validatePassword(requestPassword: String, password: String) {
        val decryptedPassword = cipher.decrypt(password)
        if(requestPassword != decryptedPassword) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
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

    fun validateIfFlagged(status: CommentStatus): Boolean {
        return status == CommentStatus.FLAGGED
    }

    fun validateIfBanned(status: RoleStatus) {
        if(status == RoleStatus.BANNED) throw AccessDeniedException(ErrorCode.BANNED, ErrorCode.BANNED.engErrorMsg)
    }

    fun validateIfIsUserRole(role: MemberRole) {
        if(role == MemberRole.ROLE_ADMIN) throw BaseException(ErrorCode.BAD_REQUEST, "CODE 1")
        if(role == MemberRole.ROLE_UNVERIFIED) throw UnauthorizedException(ErrorCode.ACCESS_DENIED, "Please verify your email first")
    }

    fun validateIfDailyChanceExceeded(dailyChance: Int) {
        if(dailyChance < 1) throw BadRequestException(ErrorCode.BAD_REQUEST, "You have exceeded your daily requests")
    }

    fun validateIfUserEmailBeenVerified(role: MemberRole) {
        if(role == MemberRole.ROLE_UNVERIFIED) throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "Please verify your email address")
    }

    fun validateIfPast5Minutes(createdDt: LocalDateTime) {
        val currentDateTime = getCurrentDateTime()
        val duration = Duration.between(createdDt, currentDateTime)
        val durationMinutes = duration.toMinutes()
        if(durationMinutes >= 5) {
            throw BadRequestException(ErrorCode.BAD_REQUEST, "5 minutes have passed")
        }
    }

    fun validateContentSize(content: String) {
        if(content.length == 2000) throw BadRequestException(ErrorCode.BAD_REQUEST, "Your content length exceeded")
    }
}