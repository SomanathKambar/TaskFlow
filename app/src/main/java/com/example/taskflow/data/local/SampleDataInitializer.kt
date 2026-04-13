package com.example.taskflow.data.local

import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataInitializer @Inject constructor(
    private val taskDao: TaskDao
) {
    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = taskDao.getAllTasks().first()
            val deletedTasks = taskDao.getDeletedTasks().first()
            
            if (tasks.isEmpty() && deletedTasks.isEmpty()) {
                val now = System.currentTimeMillis()
                
                val sampleTasks = listOf(
                    // TODAY
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Finish Android Refactor",
                        description = "Implement expert UI specifications",
                        category = "Work",
                        priority = Priority.HIGH.value,
                        status = Status.IN_PROGRESS.name,
                        dueDate = now,
                        createdAt = now
                    ),
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Buy Coffee Beans",
                        description = "Medium roast preferred",
                        category = "Shopping",
                        priority = Priority.MEDIUM.value,
                        status = Status.TODO.name,
                        dueDate = now,
                        createdAt = now
                    ),
                    // THIS WEEK
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Team Weekly Sync",
                        description = "Review progress and blockers",
                        category = "Work",
                        priority = Priority.LOW.value,
                        status = Status.TODO.name,
                        dueDate = now + TimeUnit.DAYS.toMillis(2),
                        createdAt = now
                    ),
                    // LATER
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Plan Vacation",
                        description = "Check flights and hotels",
                        category = "Personal",
                        priority = Priority.MEDIUM.value,
                        status = Status.TODO.name,
                        dueDate = now + TimeUnit.DAYS.toMillis(10),
                        createdAt = now
                    ),
                    // DELETED (In Trash)
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Old Shopping List",
                        description = "Milk, Eggs, Bread",
                        category = "Shopping",
                        priority = Priority.LOW.value,
                        status = Status.COMPLETED.name,
                        dueDate = now - TimeUnit.DAYS.toMillis(1),
                        createdAt = now - TimeUnit.DAYS.toMillis(2),
                        isDeleted = true,
                        deletedAt = now - TimeUnit.DAYS.toMillis(1) // 1 day ago
                    ),
                    TaskEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Cancelled Project Idea",
                        description = "Too ambitious for now",
                        category = "Work",
                        priority = Priority.HIGH.value,
                        status = Status.TODO.name,
                        dueDate = now,
                        createdAt = now - TimeUnit.DAYS.toMillis(8),
                        isDeleted = true,
                        deletedAt = now - TimeUnit.DAYS.toMillis(6) // 6 days ago (Urgent cleanup)
                    )
                )
                
                sampleTasks.forEach { taskDao.insertTask(it) }
            }
        }
    }
}
