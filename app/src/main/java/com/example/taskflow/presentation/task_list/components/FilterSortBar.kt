package com.example.taskflow.presentation.task_list.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortBar(
    selectedSort: SortType,
    onSortChange: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortType.values().forEach { sort ->
            FilterChip(
                selected = selectedSort == sort,
                onClick = { onSortChange(sort) },
                label = { Text(sort.name.replace("_", " ").lowercase().capitalize()) }
            )
        }
    }
}
