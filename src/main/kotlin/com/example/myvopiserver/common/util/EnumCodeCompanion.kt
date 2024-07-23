package com.example.myvopiserver.common.util

abstract class EnumCodeCompanion<V: Enum<*>, T>(
    private val valueMap: Map<T, V>
) {
    fun decode(code: T) = valueMap[code]
}