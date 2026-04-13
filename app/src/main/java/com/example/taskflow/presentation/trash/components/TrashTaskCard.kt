package com.example.taskflow.presentation.trash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Task
import com.example.taskflow.presentation.theme.ActionDelete
import com.example.taskflow.presentation.theme.ActionRestore
import java.util.concurrent.TimeUnit

@Composable
fun TrashTaskCard(
    task: Task,
    onRestore: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val deletedAt = task.deletedAt ?: System.currentTimeMillis()
    val now = System.currentTimeMillis()
    val sevenDaysInMillis = TimeUnit.DAYS.toMillis(7)
    val timeRemaining = sevenDaysInMillis - (now - deletedAt)
    val daysLeft = TimeUnit.MILLISECONDS.toDays(timeRemaining).coerceAtLeast(0)
    
    val badgeColor = if (daysLeft <= 1) Color(0xFFFF4D4F) else Color(0xFFFFA940)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough),
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${daysLeft}d left",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val daysAgo = TimeUnit.MILLISECONDS.toDays(now - deletedAt)
            Text(
                text = "Deleted $daysAgo days ago",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onRestore(task.id) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionRestore),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Restore", style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
                
                Button(
                    onClick = { onDelete(task.id) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionDelete),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
            }
        }
    }
}
