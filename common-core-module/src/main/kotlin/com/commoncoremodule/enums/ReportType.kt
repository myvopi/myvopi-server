package com.commoncoremodule.enums

import com.commoncoremodule.util.EnumCodeCompanion

enum class ReportType(
    val code: String,
    val engDef: String,
    val korDef: String,
) {
    SEXUAL("SEXUAL", "Sexual content", "성적인 콘텐츠"),
    VIOLENT("VIOLENT", "Violent or repulsive content", "폭력적 또는 혐오스러운 콘텐츠"),
    HATRED("HATRED", "Hateful or abusive content", "증오 또는 악의적인 콘텐츠"),
    HARASSMENT("HARASSMENT", "Harassment or bullying", "괴롭힘 또는 폭력"),
    HARMFUL("HARMFUL", "Harmful or dangerous acts", "유해하거나 위험한 행위"),
    MISINFORM("MISINFORM", "Misinformation", "잘못된 정보"),
    ;
    companion object : EnumCodeCompanion<ReportType, String>(entries.associateBy { it.code }) {
        fun decodeByEngDef(engDef: String): ReportType? {
            return decodeByProperty(ReportType::engDef, engDef)
        }
        fun decodeByKorDef(korDef: String): ReportType? {
            return decodeByProperty(ReportType::korDef, korDef)
        }
    }
}