package com.example.taskflow.presentation.task_list

import com.example.taskflow.domain.model.Task

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
