package com.example.myvopiserver.common.util.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentDateTime(): LocalDateTime {
    return LocalDateTime.now();
}

fun LocalDateTime.toStrings(format: String): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}