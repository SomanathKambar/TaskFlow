package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.insertTask(task)
}

class UpdateTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}

class GetTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke() = repository.getTasks()
}

class GetDeletedTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke() = repository.getDeletedTasks()
}

class SoftDeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.softDeleteTask(taskId)
}

class RestoreTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.restoreTask(taskId)
}

class HardDeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.hardDeleteTask(taskId)
}

class DeleteOldTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(threshold: Long) = repository.deleteOldTasks(threshold)
}
