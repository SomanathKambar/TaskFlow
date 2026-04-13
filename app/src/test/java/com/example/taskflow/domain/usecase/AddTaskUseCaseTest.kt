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
        val task = Task(
            id = "1",
            title = "Test Task",
            description = null,
            category = "Test",
            priority = Priority.LOW,
            status = Status.TODO,
            dueDate = null,
            createdAt = 123456789L
        )
        
        coEvery { repository.insertTask(any()) } just Runs
        
        addTaskUseCase(task)
        
        coVerify { repository.insertTask(task) }
    }
}
