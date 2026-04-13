package com.example.taskflow.domain.model

enum class SortType {
    PRIORITY, DUE_DATE, CREATED_AT
}

data class Filter(
    val category: String? = null,
    val priority: Priority? = null,
    val status: Status? = null
)
