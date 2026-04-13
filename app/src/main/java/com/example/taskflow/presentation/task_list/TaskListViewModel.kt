package com.example.taskflow.presentation.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Filter
import com.example.taskflow.domain.model.SortType
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
    private val _filter = MutableStateFlow<Filter?>(null)
    private val _sortType = MutableStateFlow(SortType.CREATED_AT)

    val state: StateFlow<TaskListUiState> = combine(
        getTasksUseCase(),
        _searchQuery,
        _filter,
        _sortType
    ) { tasks, query, filter, sort ->
        var result = tasks

        // 🔍 Search
        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                (it.description?.contains(query, ignoreCase = true) ?: false)
            }
        }

        // 🎯 Filter
        filter?.let { f ->
            result = result.filter { task ->
                (f.category == null || task.category == f.category) &&
                (f.priority == null || task.priority == f.priority) &&
                (f.status == null || task.status == f.status)
            }
        }

        // 🔄 Sorting
        result = when (sort) {
            SortType.PRIORITY -> result.sortedByDescending { it.priority.value }
            SortType.DUE_DATE -> result.sortedBy { it.dueDate ?: Long.MAX_VALUE }
            SortType.CREATED_AT -> result.sortedByDescending { it.createdAt }
        }

        TaskListUiState(
            tasks = result,
            searchQuery = query,
            selectedFilter = filter,
            sortType = sort
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TaskListUiState())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: Filter?) {
        _filter.value = filter
    }

    fun onSortChange(sort: SortType) {
        _sortType.value = sort
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
