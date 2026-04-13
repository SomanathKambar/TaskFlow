package com.example.taskflow.presentation.task_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.presentation.components.EmptyStateView
import com.example.taskflow.presentation.components.SearchBar
import com.example.taskflow.presentation.components.TaskCard

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert

import com.example.taskflow.domain.model.SortType
import com.example.taskflow.presentation.task_list.components.FilterSortBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskListUiState,
    onSearchQueryChange: (String) -> Unit,
    onSortChange: (SortType) -> Unit,
    onTaskClick: (Task) -> Unit,
    onStatusChange: (Task, Status) -> Unit,
    onAddTaskClick: () -> Unit,
    onTrashClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TaskFlow") },
                actions = {
                    IconButton(onClick = onTrashClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Trash")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = onSearchQueryChange
            )
            
            FilterSortBar(
                selectedSort = state.sortType,
                onSortChange = onSortChange
            )
            
            if (state.tasks.isEmpty() && !state.isLoading) {
                EmptyStateView(
                    message = if (state.searchQuery.isBlank()) 
                        "No tasks yet. Tap + to add one!" 
                    else 
                        "No tasks found for \"${state.searchQuery}\""
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onTaskClick = onTaskClick,
                            onStatusChange = onStatusChange
                        )
                    }
                }
            }
        }
    }
}
