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

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskflow.presentation.task_detail.TaskDetailScreen
import com.example.taskflow.presentation.task_detail.TaskDetailViewModel
import com.example.taskflow.presentation.trash.TrashScreen
import com.example.taskflow.presentation.trash.TrashViewModel

import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskFlowTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    bottomBar = {
                        val showBottomBar = currentDestination?.route in listOf("task_list", "trash")
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                                    label = { Text("Tasks") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "task_list" } == true,
                                    onClick = {
                                        navController.navigate("task_list") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                                    label = { Text("Trash") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "trash" } == true,
                                    onClick = {
                                        navController.navigate("trash") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
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
                                    onFilterChange = { category -> 
                                        viewModel.onFilterChange(category?.let { Filter(category = it) })
                                    },
                                    onTaskClick = { task -> navController.navigate("task_detail/${task.id}") },
                                    onStatusChange = viewModel::onStatusChange,
                                    onAddTaskClick = { navController.navigate("task_detail/new") }
                                )
                            }
                            composable(
                                route = "task_detail/{taskId}",
                                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
                            ) {
                                val viewModel: TaskDetailViewModel = hiltViewModel()
                                val state by viewModel.state.collectAsState()
                                
                                TaskDetailScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                            composable("trash") {
                                val viewModel: TrashViewModel = hiltViewModel()
                                val state by viewModel.state.collectAsState()
                                
                                TrashScreen(
                                    state = state,
                                    onRestoreTask = viewModel::restoreTask,
                                    onHardDeleteTask = viewModel::hardDeleteTask
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
