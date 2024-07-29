package com.example.myvopiserver.common.util.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <R: Any> R.getLogger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this.javaClass) }
}