package com.example.myvopiserver.common.config.exception

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