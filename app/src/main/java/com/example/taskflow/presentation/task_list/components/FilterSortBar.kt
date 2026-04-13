package com.example.taskflow.presentation.task_list.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Filter
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortBar(
    selectedSort: SortType,
    onSortChange: (SortType) -> Unit,
    selectedFilter: Filter?,
    onFilterChange: (Filter?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Sort, contentDescription = "Sort")
            
            SortType.values().forEach { sort ->
                FilterChip(
                    selected = selectedSort == sort,
                    onClick = { onSortChange(sort) },
                    label = { Text(sort.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Filter:", style = MaterialTheme.typography.labelMedium)
            
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onFilterChange(null) },
                label = { Text("All") }
            )

            Priority.values().forEach { priority ->
                FilterChip(
                    selected = selectedFilter?.priority == priority,
                    onClick = { 
                        onFilterChange(Filter(priority = priority))
                    },
                    label = { Text(priority.name) }
                )
            }
        }
    }
}
