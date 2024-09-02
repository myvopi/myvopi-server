package com.commoncoremodule.extension

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
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

fun getTodayBetweenDates(): BetweenDate {
    val currentDate = getCurrentDateTime()
    val fromDate = LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 0, 0, 0, 0)
    val toDate = LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 23, 59, 59, 9)
    return BetweenDate(fromDate = fromDate, toDate = toDate)
}

val dDefaultDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

fun Date.toLocalZoneDate(userTz: ZoneId): String {
    val utcZonedDateTime = this.toInstant().atZone(ZoneId.of("UTC"))
    val localZoneDate = utcZonedDateTime.withZoneSameInstant(userTz)
    return dDefaultDateFormat.format(localZoneDate)
}

fun LocalDateTime.toLocalZoneDate(userTz: ZoneId): String {
    val utcZonedDateTime = this.atZone(ZoneId.of("UTC"))
    val localZoneDate = utcZonedDateTime.withZoneSameInstant(userTz)
    return localZoneDate.format(dDefaultDateFormat)
}

data class BetweenDate(
    val fromDate: LocalDateTime,
    val toDate: LocalDateTime,
)