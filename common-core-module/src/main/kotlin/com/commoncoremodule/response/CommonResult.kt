package com.commoncoremodule.response

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.extension.getCurrentDateTime
import com.commoncoremodule.extension.toStrings

data class CommonResult<T> (
    val timestamp: String = getCurrentDateTime().toStrings("yyyy-MM-dd HH:mm:ss"),
    var path: String = "",
    var uniqueCode: String = "",
    val result: Result,
    val message: String? = null,
    val data: T? = null,
    val errorCode: ErrorCode? = null,
)
{
    enum class Result{
        SUCCESS, FAIL
    }
}