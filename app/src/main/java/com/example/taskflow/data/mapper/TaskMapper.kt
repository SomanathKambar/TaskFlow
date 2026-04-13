package com.example.taskflow.data.mapper

import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        category = category,
        priority = Priority.fromInt(priority),
        status = Status.valueOf(status),
        dueDate = dueDate,
        createdAt = createdAt,
        isDeleted = isDeleted,
        deletedAt = deletedAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        priority = priority.value,
        status = status.name,
        dueDate = dueDate,
        createdAt = createdAt,
        isDeleted = isDeleted,
        deletedAt = deletedAt
    )
}
