package com.commoncoremodule.enums

import com.commoncoremodule.util.EnumCodeCompanion

enum class SearchFilter(val def: String) {
    POPULAR("pop"),
    RECENT("rec"),
    ;
    companion object: EnumCodeCompanion<SearchFilter, String>(SearchFilter.entries.associateBy(SearchFilter::def))
}