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

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.presentation.trash.TrashScreen
import com.example.taskflow.presentation.trash.TrashViewModel

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
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "task_list"
                    ) {
                        composable("task_list") {
                            val viewModel: TaskListViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsState()
                            
                            TaskListScreen(
                                state = state,
                                onSearchQueryChange = viewModel::onSearchQueryChange,
                                onTaskClick = { /* TODO: Navigate to Edit */ },
                                onStatusChange = viewModel::onStatusChange,
                                onAddTaskClick = { /* TODO: Navigate to Add */ },
                                onTrashClick = { navController.navigate("trash") }
                            )
                        }
                        composable("trash") {
                            val viewModel: TrashViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsState()
                            
                            TrashScreen(
                                state = state,
                                onRestoreTask = viewModel::restoreTask,
                                onHardDeleteTask = viewModel::hardDeleteTask,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
