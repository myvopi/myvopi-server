package com.commoncoremodule.enums

import com.commoncoremodule.util.EnumCodeCompanion

enum class Preference(val code: String) {
    TZ("tz")
    ;
    companion object: EnumCodeCompanion<Preference, String>(Preference.entries.associateBy(Preference::code))
}