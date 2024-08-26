package com.commoncoremodule.exception

data class BaseException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class BadRequestException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class UnauthorizedException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class NotFoundException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class InternalException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class BannedException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class JwtException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}

data class AccessDeniedException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.engErrorMsg
): RuntimeException(message)
{
    override fun toString(): String {
        return super.toString()
    }
}