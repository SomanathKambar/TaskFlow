package com.example.taskflow.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Status
import com.example.taskflow.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onStatusChange: (Task, Status) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onTaskClick(task) },
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status == Status.COMPLETED,
                onCheckedChange = { isChecked ->
                    val newStatus = if (isChecked) Status.COMPLETED else Status.TODO
                    onStatusChange(task, newStatus)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriorityIndicator(priority = task.priority)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.status == Status.COMPLETED) {
                            TextDecoration.LineThrough
                        } else null
                    )
                }
                if (!task.description.isNullOrBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                CategoryChip(category = task.category)
            }
        }
    }
}
