package com.example.myvopiserver.common.handler.model

data class ResponseBodyLog(
    val traceId: String?,
    val spanId: String?,
    val remoteHost: String,
    val remotePort: Int,
    val requestURI: String,
    val method: String,
    val body: Any?,
)