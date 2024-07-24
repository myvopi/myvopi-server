package com.example.myvopiserver.common.config.response

import com.example.myvopiserver.common.config.exception.ErrorCode

class CommonResponse {

    companion object {

        fun <T : Any> success(data: T, message: String?): CommonResult<T> {
            return CommonResult(
                result = CommonResult.Result.SUCCESS,
                data = data,
                message = message,
            )
        }

        fun <T: Any> success(data: T): CommonResult<T> {
            return success(
                data = data,
                message = null
            )
        }

        fun fail(message: String, errorCode: ErrorCode): CommonResult<String> {
            return CommonResult(
                result = CommonResult.Result.FAIL,
                message = message,
                errorCode = errorCode
            )
        }

        fun fail(errorCode: ErrorCode): CommonResult<String> {
            return CommonResult(
                result = CommonResult.Result.FAIL,
                message = errorCode.engErrorMsg,
                errorCode = errorCode
            )
        }

        fun <T: Any> fail(errorCode: ErrorCode, data: T): CommonResult<T> {
            return CommonResult(
                result = CommonResult.Result.FAIL,
                message = errorCode.engErrorMsg,
                errorCode = errorCode,
                data = data
            )
        }

        fun fail(message: String): CommonResult<String> {
            return CommonResult(
                result = CommonResult.Result.FAIL,
                message = message
            )
        }
    }
}