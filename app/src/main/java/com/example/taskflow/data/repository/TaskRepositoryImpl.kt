package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.mapper.toDomain
import com.example.taskflow.data.mapper.toEntity
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDeletedTasks(): Flow<List<Task>> {
        return taskDao.getDeletedTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return taskDao.getTaskById(taskId)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun softDeleteTask(taskId: String) {
        taskDao.softDeleteTask(taskId, System.currentTimeMillis())
    }

    override suspend fun restoreTask(taskId: String) {
        taskDao.restoreTask(taskId)
    }

    override suspend fun hardDeleteTask(taskId: String) {
        taskDao.hardDeleteTask(taskId)
    }

    override suspend fun deleteOldTasks(threshold: Long) {
        taskDao.deleteOldTasks(threshold)
    }
}
