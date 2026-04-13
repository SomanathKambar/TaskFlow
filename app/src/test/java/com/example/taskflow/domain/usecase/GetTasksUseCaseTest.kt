package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetTasksUseCaseTest {

    private val repository: TaskRepository = mockk()
    private val getTasksUseCase = GetTasksUseCase(repository)

    @Test
    fun `when getting tasks, repository returns flow of tasks`() = runTest {
        val tasks = listOf(createSampleTask("Task 1"))
        every { repository.getTasks() } returns flowOf(tasks)
        val result = getTasksUseCase().first()
        assertThat(result).isEqualTo(tasks)
        verify { repository.getTasks() }
    }

    @Test
    fun `when repository is empty, it returns empty list`() = runTest {
        every { repository.getTasks() } returns flowOf(emptyList())
        val result = getTasksUseCase().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun `when repository emits multiple times, it forwards all emissions`() = runTest {
        val list1 = listOf(createSampleTask("Task 1"))
        val list2 = listOf(createSampleTask("Task 1"), createSampleTask("Task 2"))
        every { repository.getTasks() } returns flow {
            emit(list1)
            emit(list2)
        }
        val result = getTasksUseCase().take(2).toList()
        assertThat(result[0]).isEqualTo(list1)
        assertThat(result[1]).isEqualTo(list2)
    }

    private fun createSampleTask(title: String) = Task(
        id = title,
        title = title,
        description = null,
        category = "Test",
        priority = Priority.LOW,
        status = Status.TODO,
        dueDate = null,
        createdAt = 123456789L
    )
}
