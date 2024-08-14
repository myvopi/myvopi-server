package com.commoncoremodule.exception

import com.commoncoremodule.util.EnumCodeCompanion

enum class ErrorCode(val engErrorMsg: String, val korErrorMsg: String) {

    SYSTEM_ERROR("System failure, please try again later","시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    UNAUTHORIZED("Unauthorized user", "허용되지 않는 회원입니다."),
    JWT_CORRUPT("Token has bee corrupted", "토큰이 변조 되었습니다."),
    INVALID_TOKEN("Invalid token","유효하지 않은 토큰입니다."),
    ACCESS_DENIED("Access denied","접근 권한이 없습니다."),
    BAD_REQUEST("Bad request", "잘못된 요청입니다."),
    NOT_FOUND("Not found", "찾지 못했습니다."),
    EXPIRED_TOKEN("Token has expired","기한이 만료된 토큰입니다."),
    REFRESH_TOKEN_EXPIRED("Token has expired, need to re-login", "기한이 만료된 토큰입니다. 다시 로그인을 해주시기 바랍니다."),
    ;
    companion object: EnumCodeCompanion<ErrorCode, String>(ErrorCode.entries.associateBy(ErrorCode::engErrorMsg))
}