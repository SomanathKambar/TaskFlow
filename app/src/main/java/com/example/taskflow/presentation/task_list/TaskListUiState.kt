package com.example.taskflow.presentation.task_list

import com.example.taskflow.domain.model.Filter
import com.example.taskflow.domain.model.SortType
import com.example.taskflow.domain.model.Task

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: Filter? = null,
    val sortType: SortType = SortType.CREATED_AT,
    val isLoading: Boolean = false,
    val error: String? = null
)
