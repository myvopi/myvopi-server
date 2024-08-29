package com.example.myvopiserver.common.handler.model

import com.google.gson.Gson

data class ResponseBodyLog(
    val traceId: String?,
    val spanId: String?,
    val remoteHost: String,
    val remotePort: Int,
    val requestURI: String,
    val method: String,
    val body: Any?,
) {
    fun toJsonString(): String {
        return Gson().toJson(this)
    }
}