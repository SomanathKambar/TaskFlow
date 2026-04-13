package com.example.taskflow.presentation.trash

import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.GetDeletedTasksUseCase
import com.example.taskflow.domain.usecase.HardDeleteTaskUseCase
import com.example.taskflow.domain.usecase.RestoreTaskUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrashViewModelTest {

    private val getDeletedTasksUseCase: GetDeletedTasksUseCase = mockk()
    private val restoreTaskUseCase: RestoreTaskUseCase = mockk()
    private val hardDeleteTaskUseCase: HardDeleteTaskUseCase = mockk()
    private val deletedTasksFlow = MutableStateFlow<List<Task>>(emptyList())

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TrashViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getDeletedTasksUseCase() } returns deletedTasksFlow
        coEvery { restoreTaskUseCase(any()) } just runs
        coEvery { hardDeleteTaskUseCase(any()) } just runs
        viewModel = TrashViewModel(getDeletedTasksUseCase, restoreTaskUseCase, hardDeleteTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state reflects deleted tasks from use case`() = runTest {
        deletedTasksFlow.value = listOf(
            createTask(id = "trash-1"),
            createTask(id = "trash-2", title = "Archived notes")
        )

        val state = viewModel.state.first { it.deletedTasks.isNotEmpty() }

        assertThat(state.deletedTasks.map { it.id })
            .containsExactly("trash-1", "trash-2")
            .inOrder()
    }

    @Test
    fun `restore delegates to restore use case`() = runTest {
        viewModel.restoreTask("trash-restore")
        advanceUntilIdle()

        coVerify { restoreTaskUseCase("trash-restore") }
    }

    @Test
    fun `hard delete delegates to hard delete use case`() = runTest {
        viewModel.hardDeleteTask("trash-delete")
        advanceUntilIdle()

        coVerify { hardDeleteTaskUseCase("trash-delete") }
    }

    private fun createTask(
        id: String,
        title: String = "Task $id"
    ) = Task(
        id = id,
        title = title,
        description = null,
        category = "Work",
        priority = Priority.LOW,
        status = Status.TODO,
        dueDate = null,
        createdAt = 1000L,
        isDeleted = true,
        deletedAt = 900L
    )
}
