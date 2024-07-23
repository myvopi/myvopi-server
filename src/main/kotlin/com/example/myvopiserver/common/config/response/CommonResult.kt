package com.example.myvopiserver.common.config.response

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.util.extension.getCurrentDateTime
import com.example.myvopiserver.common.util.extension.toStrings

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