package com.commoncoremodule.util

abstract class EnumCodeCompanion<V: Enum<*>, T>(
    private val valueMap: Map<T, V>
) {
    fun decode(code: T) = valueMap[code]

    fun decodeByProperty(property: (V) -> String, value: String): V? {
        return valueMap.keys.find { key ->
            valueMap[key]?.let { property(it) == value } ?: false
        }?.let { valueMap[it] }
    }
}