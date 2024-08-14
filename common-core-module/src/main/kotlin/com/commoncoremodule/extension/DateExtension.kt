package com.commoncoremodule.extension

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDateTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun LocalDateTime.toStrings(format: String): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}

fun Date.toStrings(format: String): String {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(this)
}