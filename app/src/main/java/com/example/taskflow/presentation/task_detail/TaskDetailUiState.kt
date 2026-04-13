package com.example.taskflow.presentation.task_detail

import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status

data class TaskDetailUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "General",
    val priority: Priority = Priority.LOW,
    val status: Status = Status.TODO,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val dueDate:Long? = null,
)

sealed class TaskDetailEvent {
    data class TitleChanged(val title: String) : TaskDetailEvent()
    data class DescriptionChanged(val description: String) : TaskDetailEvent()
    data class CategoryChanged(val category: String) : TaskDetailEvent()
    data class PriorityChanged(val priority: Priority) : TaskDetailEvent()
    data class StatusChanged(val status: Status) : TaskDetailEvent()
    data class DueDateChanged(val dueDate: Long?) : TaskDetailEvent()
    object SaveTask : TaskDetailEvent()
}
