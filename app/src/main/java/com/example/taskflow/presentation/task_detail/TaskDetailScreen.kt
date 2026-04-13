package com.example.taskflow.presentation.task_detail

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import com.example.taskflow.presentation.components.FilterChips
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    state: TaskDetailUiState,
    onEvent: (TaskDetailEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val calendar = remember { Calendar.getInstance() }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBackClick()
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis
            onEvent(TaskDetailEvent.DueDateChanged(selectedDate))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onEvent(TaskDetailEvent.SaveTask) },
                        enabled = state.title.isNotBlank()
                    ) {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (state.title.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Title", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(TaskDetailEvent.TitleChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    ),
                    placeholder = { Text("Enter task title...") },
                    singleLine = true
                )
            }

            // Description
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Description", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                TextField(
                    value = state.description,
                    onValueChange = { onEvent(TaskDetailEvent.DescriptionChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    ),
                    placeholder = { Text("Enter description...") },
                    minLines = 3
                )
            }

            // Category
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Category", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                FilterChips(
                    options = listOf("Work", "Personal", "Shopping", "General"),
                    selectedOption = state.category,
                    onOptionSelected = { it?.let { onEvent(TaskDetailEvent.CategoryChanged(it)) } }
                )
            }

            // Priority
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Priority", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    Priority.values().forEachIndexed { index, priority ->
                        SegmentedButton(
                            selected = state.priority == priority,
                            onClick = { onEvent(TaskDetailEvent.PriorityChanged(priority)) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = Priority.values().size)
                        ) {
                            Text(priority.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }

            // Due Date
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Due Date", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                OutlinedCard(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (state.dueDate != null) dateFormat.format(Date(state.dueDate)) else "Pick a date",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                }
            }

            // Status
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Status", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                FilterChips(
                    options = Status.values().map { it.name.replace("_", " ") },
                    selectedOption = state.status.name.replace("_", " "),
                    onOptionSelected = { it?.let { onEvent(TaskDetailEvent.StatusChanged(Status.valueOf(it.replace(" ", "_")))) } }
                )
            }
        }
    }
}
