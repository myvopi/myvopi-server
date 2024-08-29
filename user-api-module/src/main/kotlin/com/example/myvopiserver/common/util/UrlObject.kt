package com.example.myvopiserver.common.util

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class UrlObject {

    private val requestMatchers: MutableMap<String, List<String>> = HashMap()

    // TODO dynamically refactor
    @PostConstruct
    fun init() {
        requestMatchers["/op/api/v1/comment"] = listOf("GET")
        requestMatchers["/cv/api/v1/comment"] = listOf("PUT", "POST", "DELETE")
        requestMatchers["/cv/api/v1/comment/like"] = listOf("POST")
        requestMatchers["/cv/api/v1/comment/unlike"] = listOf("POST")
        requestMatchers["/cv/api/v1/comment/report"] = listOf("POST")
        requestMatchers["/op/api/v1/reply"] = listOf("GET")
        requestMatchers["/cv/api/v1/reply"] = listOf("PUT", "POST", "DELETE")
        requestMatchers["/cv/api/v1/reply/like"] = listOf("POST")
        requestMatchers["/cv/api/v1/reply/unlike"] = listOf("POST")
        requestMatchers["/cv/api/v1/reply/report"] = listOf("POST")
        requestMatchers["/op/api/v1/user/register"] = listOf("POST")
        requestMatchers["/op/api/v1/user/login"] = listOf("POST")
        requestMatchers["/cv/api/v1/user/register/email/verification/newCode"] = listOf("POST")
        requestMatchers["/cv/api/v1/user/register/email/verification"] = listOf("POST")
        requestMatchers["/op/api/v1/user/token/re-issue"] = listOf("POST")
        requestMatchers["/watch"] = listOf("GET")
        requestMatchers["/cv/api/v1/user/my-info"] = listOf("POST")
    }

    fun requestMatchers(): MutableMap<String, List<String>> {
        return this.requestMatchers
    }
}