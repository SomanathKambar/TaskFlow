package com.example.taskflow.presentation.task_list

import com.example.taskflow.domain.model.Filter
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.SortType
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.GetTasksUseCase
import com.example.taskflow.domain.usecase.SoftDeleteTaskUseCase
import com.example.taskflow.domain.usecase.UpdateTaskUseCase
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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val updateTaskUseCase: UpdateTaskUseCase = mockk()
    private val softDeleteTaskUseCase: SoftDeleteTaskUseCase = mockk()
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    private lateinit var viewModel: TaskListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getTasksUseCase() } returns tasksFlow
        coEvery { updateTaskUseCase(any()) } just runs
        coEvery { softDeleteTaskUseCase(any()) } just runs
        viewModel = TaskListViewModel(getTasksUseCase, updateTaskUseCase, softDeleteTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search matches title and description ignoring case`() = runTest {
        tasksFlow.value = listOf(
            createTask(id = "1", title = "Release Notes", description = "Share beta build"),
            createTask(id = "2", title = "Groceries", description = "Pick up oat milk")
        )

        viewModel.onSearchQueryChange("beta")
        advanceUntilIdle()

        assertThat(viewModel.state.value.tasks.map { it.id }).containsExactly("1")
    }

    @Test
    fun `filter applies category priority and status together`() = runTest {
        tasksFlow.value = listOf(
            createTask(id = "1", category = "Work", priority = Priority.HIGH, status = Status.IN_PROGRESS),
            createTask(id = "2", category = "Work", priority = Priority.LOW, status = Status.IN_PROGRESS),
            createTask(id = "3", category = "Personal", priority = Priority.HIGH, status = Status.IN_PROGRESS)
        )

        viewModel.onFilterChange(
            Filter(category = "Work", priority = Priority.HIGH, status = Status.IN_PROGRESS)
        )
        advanceUntilIdle()

        assertThat(viewModel.state.value.tasks.map { it.id }).containsExactly("1")
    }

    @Test
    fun `sort by due date keeps earliest due date first and undated tasks last`() = runTest {
        tasksFlow.value = listOf(
            createTask(id = "1", dueDate = null),
            createTask(id = "2", dueDate = 500L),
            createTask(id = "3", dueDate = 100L)
        )

        viewModel.onSortChange(SortType.DUE_DATE)
        advanceUntilIdle()

        assertThat(viewModel.state.value.tasks.map { it.id }).containsExactly("3", "2", "1").inOrder()
    }

    @Test
    fun `sort by priority shows high priority items first`() = runTest {
        tasksFlow.value = listOf(
            createTask(id = "1", priority = Priority.LOW),
            createTask(id = "2", priority = Priority.HIGH),
            createTask(id = "3", priority = Priority.MEDIUM)
        )

        viewModel.onSortChange(SortType.PRIORITY)
        advanceUntilIdle()

        assertThat(viewModel.state.value.tasks.map { it.id }).containsExactly("2", "3", "1").inOrder()
    }

    @Test
    fun `status change updates task with new status`() = runTest {
        val task = createTask(id = "task-1", status = Status.TODO)

        viewModel.onStatusChange(task, Status.COMPLETED)
        advanceUntilIdle()

        coVerify { updateTaskUseCase(task.copy(status = Status.COMPLETED)) }
    }

    @Test
    fun `soft delete delegates to use case`() = runTest {
        viewModel.softDeleteTask("task-42")
        advanceUntilIdle()

        coVerify { softDeleteTaskUseCase("task-42") }
    }

    private fun createTask(
        id: String,
        title: String = "Task $id",
        description: String? = null,
        category: String = "Work",
        priority: Priority = Priority.LOW,
        status: Status = Status.TODO,
        dueDate: Long? = null,
        createdAt: Long = 1000L
    ) = Task(
        id = id,
        title = title,
        description = description,
        category = category,
        priority = priority,
        status = status,
        dueDate = dueDate,
        createdAt = createdAt
    )
}
