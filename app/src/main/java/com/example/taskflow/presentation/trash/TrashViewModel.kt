package com.example.taskflow.presentation.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.usecase.GetDeletedTasksUseCase
import com.example.taskflow.domain.usecase.HardDeleteTaskUseCase
import com.example.taskflow.domain.usecase.RestoreTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val getDeletedTasksUseCase: GetDeletedTasksUseCase,
    private val restoreTaskUseCase: RestoreTaskUseCase,
    private val hardDeleteTaskUseCase: HardDeleteTaskUseCase
) : ViewModel() {

    val state = getDeletedTasksUseCase()
        .map { tasks -> TrashUiState(deletedTasks = tasks) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrashUiState())

    fun restoreTask(taskId: String) {
        viewModelScope.launch {
            restoreTaskUseCase(taskId)
        }
    }

    fun hardDeleteTask(taskId: String) {
        viewModelScope.launch {
            hardDeleteTaskUseCase(taskId)
        }
    }
}
