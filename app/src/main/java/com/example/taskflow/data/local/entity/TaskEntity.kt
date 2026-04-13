package com.example.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val category: String,
    val priority: Int, // 0: LOW, 1: MEDIUM, 2: HIGH
    val status: String, // TODO, IN_PROGRESS, COMPLETED
    val dueDate: Long?,
    val createdAt: Long,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
