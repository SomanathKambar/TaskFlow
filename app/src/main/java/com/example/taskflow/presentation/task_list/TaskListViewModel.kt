package com.example.taskflow.presentation.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val softDeleteTaskUseCase: SoftDeleteTaskUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _state = MutableStateFlow(TaskListUiState())
    val state = combine(
        getTasksUseCase(),
        _searchQuery
    ) { tasks, query ->
        val filteredTasks = if (query.isBlank()) {
            tasks
        } else {
            tasks.filter {
                it.title.contains(query, ignoreCase = true) ||
                (it.description?.contains(query, ignoreCase = true) ?: false)
            }
        }
        
        TaskListUiState(
            tasks = filteredTasks.sortedByDescending { it.createdAt },
            searchQuery = query
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskListUiState())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onStatusChange(task: Task, newStatus: Status) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(status = newStatus))
        }
    }

    fun softDeleteTask(taskId: String) {
        viewModelScope.launch {
            softDeleteTaskUseCase(taskId)
        }
    }
}
