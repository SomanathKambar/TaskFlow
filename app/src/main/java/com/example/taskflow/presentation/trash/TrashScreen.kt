package com.example.taskflow.presentation.trash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.presentation.components.EmptyStateView
import com.example.taskflow.presentation.trash.components.InfoBanner
import com.example.taskflow.presentation.trash.components.TrashTaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    state: TrashUiState,
    onRestoreTask: (String) -> Unit,
    onHardDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "Trash",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            InfoBanner(message = "Auto-deleted after 7 days")
            
            if (state.deletedTasks.isEmpty()) {
                EmptyStateView(
                    message = "Trash is empty",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.deletedTasks, key = { it.id }) { task ->
                        TrashTaskCard(
                            task = task,
                            onRestore = onRestoreTask,
                            onDelete = onHardDeleteTask
                        )
                    }
                }
            }
        }
    }
}
