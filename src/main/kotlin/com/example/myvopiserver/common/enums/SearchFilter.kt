package com.example.myvopiserver.common.enums

import com.example.myvopiserver.common.util.EnumCodeCompanion

enum class SearchFilter(val def: String) {
    POPULAR("pop"),
    RECENT("rec"),
    ;
    companion object: EnumCodeCompanion<SearchFilter, String>(SearchFilter.entries.associateBy(SearchFilter::def))
}