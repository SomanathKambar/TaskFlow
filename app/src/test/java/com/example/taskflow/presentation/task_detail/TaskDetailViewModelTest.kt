package com.example.taskflow.presentation.task_detail

import androidx.lifecycle.SavedStateHandle
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.AddTaskUseCase
import com.example.taskflow.domain.usecase.GetTaskByIdUseCase
import com.example.taskflow.domain.usecase.UpdateTaskUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    private val getTaskByIdUseCase: GetTaskByIdUseCase = mockk()
    private val addTaskUseCase: AddTaskUseCase = mockk()
    private val updateTaskUseCase: UpdateTaskUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { addTaskUseCase(any()) } just runs
        coEvery { updateTaskUseCase(any()) } just runs
        coEvery { getTaskByIdUseCase(any()) } returns null
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `blank title blocks save and surfaces validation error`() = runTest {
        val viewModel = createViewModel(taskId = "new")

        viewModel.onEvent(TaskDetailEvent.SaveTask)
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo("Title cannot be empty")
        coVerify(exactly = 0) { addTaskUseCase(any()) }
        coVerify(exactly = 0) { updateTaskUseCase(any()) }
    }

    @Test
    fun `new task save calls add use case and marks state saved`() = runTest {
        val viewModel = createViewModel(taskId = "new")

        viewModel.onEvent(TaskDetailEvent.TitleChanged("Draft release notes"))
        viewModel.onEvent(TaskDetailEvent.DescriptionChanged("Include known issues"))
        viewModel.onEvent(TaskDetailEvent.CategoryChanged("Work"))
        viewModel.onEvent(TaskDetailEvent.PriorityChanged(Priority.HIGH))
        viewModel.onEvent(TaskDetailEvent.StatusChanged(Status.IN_PROGRESS))
        viewModel.onEvent(TaskDetailEvent.DueDateChanged(1234L))
        viewModel.onEvent(TaskDetailEvent.SaveTask)
        advanceUntilIdle()

        coVerify {
            addTaskUseCase(
                withArg { task ->
                    assertThat(task.id).isNotEmpty()
                    assertThat(task.title).isEqualTo("Draft release notes")
                    assertThat(task.description).isEqualTo("Include known issues")
                    assertThat(task.category).isEqualTo("Work")
                    assertThat(task.priority).isEqualTo(Priority.HIGH)
                    assertThat(task.status).isEqualTo(Status.IN_PROGRESS)
                    assertThat(task.dueDate).isEqualTo(1234L)
                }
            )
        }
        assertThat(viewModel.state.value.isSaved).isTrue()
    }

    @Test
    fun `existing task is loaded into state`() = runTest {
        val existingTask = createTask(
            id = "task-1",
            title = "Prepare QA checklist",
            description = "Cover regression cases",
            category = "General",
            priority = Priority.MEDIUM,
            status = Status.IN_PROGRESS,
            dueDate = 4567L,
            createdAt = 999L
        )
        coEvery { getTaskByIdUseCase("task-1") } returns existingTask

        val viewModel = createViewModel(taskId = "task-1")
        advanceUntilIdle()

        assertThat(viewModel.state.value.title).isEqualTo("Prepare QA checklist")
        assertThat(viewModel.state.value.description).isEqualTo("Cover regression cases")
        assertThat(viewModel.state.value.category).isEqualTo("General")
        assertThat(viewModel.state.value.priority).isEqualTo(Priority.MEDIUM)
        assertThat(viewModel.state.value.status).isEqualTo(Status.IN_PROGRESS)
        assertThat(viewModel.state.value.dueDate).isEqualTo(4567L)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `existing task save preserves original createdAt`() = runTest {
        val existingTask = createTask(id = "task-2", createdAt = 321L)
        coEvery { getTaskByIdUseCase("task-2") } returns existingTask

        val viewModel = createViewModel(taskId = "task-2")
        advanceUntilIdle()

        viewModel.onEvent(TaskDetailEvent.TitleChanged("Updated title"))
        viewModel.onEvent(TaskDetailEvent.SaveTask)
        advanceUntilIdle()

        coVerify {
            updateTaskUseCase(
                withArg { task ->
                    assertThat(task.id).isEqualTo("task-2")
                    assertThat(task.title).isEqualTo("Updated title")
                    assertThat(task.createdAt).isEqualTo(321L)
                }
            )
        }
    }

    @Test
    fun `missing task stops loading and shows error`() = runTest {
        coEvery { getTaskByIdUseCase("missing-task") } returns null

        val viewModel = createViewModel(taskId = "missing-task")
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.error).isEqualTo("Task not found")
    }

    private fun createViewModel(taskId: String): TaskDetailViewModel {
        return TaskDetailViewModel(
            getTaskByIdUseCase = getTaskByIdUseCase,
            addTaskUseCase = addTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            savedStateHandle = SavedStateHandle(mapOf("taskId" to taskId))
        )
    }

    private fun createTask(
        id: String,
        title: String = "Task $id",
        description: String? = "Description",
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
