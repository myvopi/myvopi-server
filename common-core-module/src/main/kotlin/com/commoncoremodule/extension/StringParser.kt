package com.commoncoremodule.extension

import com.commoncoremodule.enums.Preference
import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.ErrorCode
import java.time.ZoneId

fun parsePreferences(preferences: String): Map<Preference, Any> {
    val wholePref = preferences.substringAfter("pref=")
    val splitPref = wholePref.split("&")
    return splitPref.associate { pref ->
        val split = pref.split("=")
        when {
            split[0] == Preference.TZ.code -> {
                try {
                    Preference.TZ to ZoneId.of(split[1])
                } catch (e: Exception) {
                    throw BadRequestException(ErrorCode.BAD_REQUEST)
                }
            }
            else -> {
                throw BadRequestException(ErrorCode.BAD_REQUEST)
            }
        }
    }
}