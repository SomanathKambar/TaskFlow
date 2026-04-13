package com.example.taskflow.presentation.task_list

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Filter
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task
import com.example.taskflow.presentation.components.EmptyStateView
import com.example.taskflow.presentation.components.FilterChips
import com.example.taskflow.presentation.components.SearchBar
import com.example.taskflow.presentation.components.TaskCard
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskListUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (String?) -> Unit,
    onTaskClick: (Task) -> Unit,
    onStatusChange: (Task, Status) -> Unit,
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = Calendar.getInstance()
    val todayTasks = state.tasks.filter { 
        it.dueDate != null && DateUtils.isToday(it.dueDate) 
    }
    val thisWeekTasks = state.tasks.filter { 
        it.dueDate != null && !DateUtils.isToday(it.dueDate) && it.dueDate < today.timeInMillis + 7 * 24 * 60 * 60 * 1000L
    }
    val otherTasks = state.tasks.filter { 
        it.dueDate == null || (it.dueDate >= today.timeInMillis + 7 * 24 * 60 * 60 * 1000L) 
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "My Tasks",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FilterChips(
                options = listOf("All", "Work", "Personal", "Shopping"),
                selectedOption = state.selectedFilter?.category,
                onOptionSelected = onFilterChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (todayTasks.isNotEmpty()) {
                        item { SectionHeader("TODAY") }
                        items(todayTasks, key = { it.id }) { task ->
                            TaskCard(task, onTaskClick, onStatusChange)
                        }
                    }
                    
                    if (thisWeekTasks.isNotEmpty()) {
                        item { SectionHeader("THIS WEEK") }
                        items(thisWeekTasks, key = { it.id }) { task ->
                            TaskCard(task, onTaskClick, onStatusChange)
                        }
                    }
                    
                    if (otherTasks.isNotEmpty()) {
                        if (todayTasks.isNotEmpty() || thisWeekTasks.isNotEmpty()) {
                            item { SectionHeader("LATER") }
                        }
                        items(otherTasks, key = { it.id }) { task ->
                            TaskCard(task, onTaskClick, onStatusChange)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
