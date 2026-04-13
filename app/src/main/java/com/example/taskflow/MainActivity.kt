package com.example.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.presentation.task_list.TaskListScreen
import com.example.taskflow.presentation.task_list.TaskListViewModel
import com.example.taskflow.presentation.theme.TaskFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: TaskListViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsState()
                    
                    TaskListScreen(
                        state = state,
                        onSearchQueryChange = viewModel::onSearchQueryChange,
                        onTaskClick = { /* TODO: Navigate to Edit */ },
                        onStatusChange = viewModel::onStatusChange,
                        onAddTaskClick = { /* TODO: Navigate to Add */ }
                    )
                }
            }
        }
    }
}
