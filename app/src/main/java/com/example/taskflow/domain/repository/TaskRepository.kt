package com.example.taskflow.domain.repository

import com.example.taskflow.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getDeletedTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun softDeleteTask(taskId: String)
    suspend fun restoreTask(taskId: String)
    suspend fun hardDeleteTask(taskId: String)
    suspend fun deleteOldTasks(threshold: Long)
}
