package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTaskUseCaseTest {

    private lateinit var addTaskUseCase: AddTaskUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        addTaskUseCase = AddTaskUseCase(repository)
    }

    @Test
    fun `when adding a task, repository is called with the task`() = runTest {
        val task = createSampleTask("Test Task")
        coEvery { repository.insertTask(any()) } just Runs
        addTaskUseCase(task)
        coVerify { repository.insertTask(task) }
    }

    @Test
    fun `when adding a task with empty title, it still calls repository`() = runTest {
        // Business logic for validation usually lives in UseCase or ViewModel.
        // Currently, our UseCase is a simple wrapper.
        val task = createSampleTask("")
        coEvery { repository.insertTask(any()) } just Runs
        addTaskUseCase(task)
        coVerify { repository.insertTask(task) }
    }

    @Test
    fun `when adding a task with very long description, it still calls repository`() = runTest {
        val longDescription = "a".repeat(1000)
        val task = createSampleTask("Long Desc Task", longDescription)
        coEvery { repository.insertTask(any()) } just Runs
        addTaskUseCase(task)
        coVerify { repository.insertTask(task) }
    }

    private fun createSampleTask(title: String, description: String? = null) = Task(
        id = "1",
        title = title,
        description = description,
        category = "Test",
        priority = Priority.LOW,
        status = Status.TODO,
        dueDate = null,
        createdAt = 123456789L
    )
}
