package com.example.myvopiserver.common.handler.model

import org.springframework.util.MultiValueMap
import java.util.*

data class RequestLog (
    val date: Date = Date(),
    val traceId: String?,
    val spanId: String?,
    val remoteHost: String,
    val remotePort: Int,
    val requestURI: String,
    val method: String,
    val headers: MultiValueMap<String, String>,
    val parameters: MultiValueMap<String, String>,
    val body: String?,
)