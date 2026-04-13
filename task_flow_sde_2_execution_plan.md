# TaskFlow – Smart Task Manager (SDE-2 Execution Plan)

## 🎯 Project Goal
Build a production-grade offline-first Android application using Kotlin + Jetpack Compose that enables users to manage tasks efficiently with features like CRUD, search, filtering, soft delete, and automated cleanup.

This project emphasizes:
- Clean Architecture
- Reactive data flow using Kotlin Flow
- Scalable UI using Jetpack Compose
- Production-level engineering practices

---

# 🧠 Architecture Overview

```
UI (Compose)
   ↓
ViewModel (StateFlow)
   ↓
UseCases (Business Logic)
   ↓
Repository (Flow combination logic)
   ↓
Room Database (Single Source of Truth)
```

---

# 🔁 Repository + Flow Combination Logic (CORE DESIGN)

## 🎯 Objective
Combine multiple dynamic inputs:
- Task list from DB
- Search query
- Filters
- Sorting

## 🔹 Inputs as Flows

```
val tasksFlow: Flow<List<TaskEntity>>
val searchQueryFlow: MutableStateFlow<String>
val filterFlow: MutableStateFlow<Filter?>
val sortFlow: MutableStateFlow<SortType>
```

## 🔹 Combined Flow

```
combine(
    tasksFlow,
    searchQueryFlow,
    filterFlow,
    sortFlow
) { tasks, query, filter, sort ->

    var result = tasks

    // 🔍 Search
    if (query.isNotBlank()) {
        result = result.filter {
            it.title.contains(query, true) ||
            (it.description?.contains(query, true) ?: false)
        }
    }

    // 🎯 Filter
    filter?.let {
        result = result.filter { task ->
            task.category == it.category ||
            task.priority == it.priority ||
            task.status == it.status
        }
    }

    // 🔄 Sorting
    result = when (sort) {
        SortType.PRIORITY -> result.sortedBy { it.priority }
        SortType.DUE_DATE -> result.sortedBy { it.dueDate ?: Long.MAX_VALUE }
        SortType.CREATED_AT -> result.sortedByDescending { it.createdAt }
    }

    result.map { it.toDomain() }
}
```

## 🔥 Why This Matters
- Single reactive pipeline
- No manual refresh needed
- Scales easily for new filters
- Interview-level strong design

---

# 📱 UI Composable Structure

## Screens
- TaskListScreen
- CreateTaskScreen
- EditTaskScreen
- TrashScreen

## Reusable Components
- TaskCard
- PriorityIndicator
- CategoryChip
- StatusCheckbox
- TaskInputField
- EmptyStateView
- SearchBar
- FilterChips

---

# 🔄 ViewModel + UI State Flow

## UI State

```
data class TaskListUiState(
    val tasks: List<TaskUiModel> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: Filter? = null,
    val sortType: SortType = SortType.PRIORITY,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
```

## Event Handling

```
sealed class TaskListEvent {
    data class OnSearchChange(val query: String)
    data class OnFilterChange(val filter: Filter?)
    data class OnSortChange(val sort: SortType)
    data class OnStatusChange(val id: String, val status: Status)
}
```

---

# 🧩 Feature Tickets (JIRA Style)

## EPIC 1: Setup
- Setup Compose, Hilt, Room, WorkManager
- Define package structure

## EPIC 2: Data Layer
- TaskEntity + DAO
- Room DB setup

## EPIC 3: Domain Layer
- UseCases (CRUD, Search, Restore, Delete)

## EPIC 4: UI
- TaskListScreen
- Create/Edit Screens
- Reusable components

## EPIC 5: Trash
- Trash screen
- Restore / Permanent delete

## EPIC 6: Search & Filter
- Real-time search
- Sorting
- Filtering

## EPIC 7: Cleanup
- WorkManager job (7-day deletion)

## EPIC 8: Testing
- UseCase tests
- DAO tests

---

# 🚀 Step-by-Step Coding Plan

## Phase 1 (0–2 hrs)
- Setup project
- Add dependencies
- Create base architecture

## Phase 2 (2–4 hrs)
- Implement TaskEntity + DAO
- Setup Room DB

## Phase 3 (4–6 hrs)
- Create UseCases
- Implement Repository with Flow combination

## Phase 4 (6–8 hrs)
- Build TaskListScreen UI (static)
- Create reusable composables

## Phase 5 (8–10 hrs)
- Connect ViewModel + UI
- Implement search/filter logic

## Phase 6 (10–12 hrs)
- Trash feature
- Soft delete logic

## Phase 7 (12–14 hrs)
- WorkManager cleanup

## Phase 8 (14–16 hrs)
- Testing + README

---

# 🧪 Testing Strategy
- Unit tests for UseCases
- DAO tests using in-memory DB
- ViewModel state tests

---

# ⚠️ Assumptions
- Single category per task
- Offline-only app
- Case-insensitive search
- Cleanup runs every 24 hours

---

# 🔮 Future Improvements
- Multi-tag support
- Cloud sync
- Pagination
- Push notifications for due tasks

---

# 🧾 Conclusion

This project is designed to demonstrate SDE-2 level thinking with:
- Clean separation of concerns
- Reactive programming
- Scalable architecture
- Production-ready UI

It prioritizes maintainability, extensibility, and clarity over quick hacks.

