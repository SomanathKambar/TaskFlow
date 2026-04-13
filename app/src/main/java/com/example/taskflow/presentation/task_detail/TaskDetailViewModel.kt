package com.example.taskflow.presentation.task_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.AddTaskUseCase
import com.example.taskflow.domain.usecase.GetTaskByIdUseCase
import com.example.taskflow.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailUiState())
    val state = _state.asStateFlow()

    private var currentTaskId: String? = null

    init {
        savedStateHandle.get<String>("taskId")?.let { taskId ->
            if (taskId != "new") {
                loadTask(taskId)
            }
        }
    }

    private fun loadTask(taskId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getTaskByIdUseCase(taskId)?.let { task ->
                currentTaskId = task.id
                _state.update {
                    it.copy(
                        title = task.title,
                        description = task.description ?: "",
                        category = task.category,
                        priority = task.priority,
                        status = task.status,
                        dueDate = task.dueDate,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.TitleChanged -> {
                _state.update { it.copy(title = event.title) }
            }
            is TaskDetailEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }
            is TaskDetailEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.category) }
            }
            is TaskDetailEvent.PriorityChanged -> {
                _state.update { it.copy(priority = event.priority) }
            }
            is TaskDetailEvent.StatusChanged -> {
                _state.update { it.copy(status = event.status) }
            }
            is TaskDetailEvent.DueDateChanged -> {
                _state.update { it.copy(dueDate = event.dueDate) }
            }
            TaskDetailEvent.SaveTask -> {
                saveTask()
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val s = _state.value
            if (s.title.isBlank()) {
                _state.update { it.copy(error = "Title cannot be empty") }
                return@launch
            }

            val task = Task(
                id = currentTaskId ?: UUID.randomUUID().toString(),
                title = s.title,
                description = s.description,
                category = s.category,
                priority = s.priority,
                status = s.status,
                dueDate = s.dueDate,
                createdAt = System.currentTimeMillis()
            )

            if (currentTaskId == null) {
                addTaskUseCase(task)
            } else {
                updateTaskUseCase(task)
            }
            _state.update { it.copy(isSaved = true) }
        }
    }
}
