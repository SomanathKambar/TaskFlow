package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.repository.TaskRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteOldTasksUseCaseTest {

    private val repository: TaskRepository = mockk()
    private val deleteOldTasksUseCase = DeleteOldTasksUseCase(repository)

    @Test
    fun `when deleting old tasks, repository is called with correct threshold`() = runTest {
        val threshold = 1000L
        coEvery { repository.deleteOldTasks(any()) } just Runs
        
        deleteOldTasksUseCase(threshold)
        
        coVerify { repository.deleteOldTasks(threshold) }
    }
}
