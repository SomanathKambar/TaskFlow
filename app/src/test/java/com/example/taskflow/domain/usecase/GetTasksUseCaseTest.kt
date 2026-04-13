package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetTasksUseCaseTest {

    private val repository: TaskRepository = mockk()
    private val getTasksUseCase = GetTasksUseCase(repository)

    @Test
    fun `when getting tasks, repository returns flow of tasks`() = runTest {
        val tasks = listOf(
            Task(
                id = "1",
                title = "Test Task",
                description = null,
                category = "Test",
                priority = Priority.LOW,
                status = Status.TODO,
                dueDate = null,
                createdAt = 123456789L
            )
        )
        
        every { repository.getTasks() } returns flowOf(tasks)
        
        val result = getTasksUseCase().first()
        
        assertThat(result).isEqualTo(tasks)
        verify { repository.getTasks() }
    }
}
