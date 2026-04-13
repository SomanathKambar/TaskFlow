package com.example.taskflow.presentation.trash

import com.example.taskflow.domain.model.Task

data class TrashUiState(
    val deletedTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
