package com.example.adminmyvopiserver.common

import jakarta.validation.constraints.Size
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

data class PageableDto(
    @Size(min = 0)
    val page: Int,
    @Size(min = 1)
    val size: Int,
    val sorts: List<SortDto>,
)
{

    data class SortDto(
        val direction: Sort.Direction,
        val property: String,
    )
    {
        fun toSortOrder(): Sort.Order
        {
            return Sort.Order(
                direction,
                property
            )
        }
    }

    fun toPageable(): Pageable
    {
        return PageRequest.of(
            page,
            size,
            Sort.by(sorts.map { it.toSortOrder() })
        )
    }
}