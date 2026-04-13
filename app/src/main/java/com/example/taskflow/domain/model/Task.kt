package com.example.taskflow.domain.model

enum class Priority(val value: Int) {
    LOW(0), MEDIUM(1), HIGH(2);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

enum class Status {
    TODO, IN_PROGRESS, COMPLETED
}

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val category: String,
    val priority: Priority,
    val status: Status,
    val dueDate: Long?,
    val createdAt: Long,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
