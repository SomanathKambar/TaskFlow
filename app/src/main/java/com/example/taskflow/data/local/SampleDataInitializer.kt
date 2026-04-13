package com.example.taskflow.data.local

import android.content.Context
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskDao: TaskDao
) {
    private val preferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            if (preferences.getInt(KEY_SAMPLE_VERSION, 0) >= SAMPLE_VERSION) {
                return@launch
            }

            buildSampleTasks(System.currentTimeMillis()).forEach { task ->
                if (taskDao.getTaskById(task.id) == null) {
                    taskDao.insertTask(task)
                }
            }

            preferences.edit().putInt(KEY_SAMPLE_VERSION, SAMPLE_VERSION).apply()
        }
    }

    private fun buildSampleTasks(now: Long): List<TaskEntity> {
        return listOf(
            TaskEntity(
                id = SAMPLE_TASK_TODAY_IN_PROGRESS,
                title = "Finish Android Refactor",
                description = "Implement the updated UI specifications and validate every section visually.",
                category = "Work",
                priority = Priority.HIGH.value,
                status = Status.IN_PROGRESS.name,
                dueDate = now,
                createdAt = now - TimeUnit.HOURS.toMillis(6)
            ),
            TaskEntity(
                id = SAMPLE_TASK_TODAY_LONG_TITLE,
                title = "Buy Coffee Beans, Filters, Oat Milk, and the backup snacks for tomorrow's sprint review",
                description = "Long title sample to check wrapping, spacing, and card balance on smaller devices.",
                category = "Shopping",
                priority = Priority.MEDIUM.value,
                status = Status.TODO.name,
                dueDate = now,
                createdAt = now - TimeUnit.HOURS.toMillis(4)
            ),
            TaskEntity(
                id = SAMPLE_TASK_WEEK_PERSONAL,
                title = "Doctor Follow-up Appointment",
                description = "",
                category = "Personal",
                priority = Priority.HIGH.value,
                status = Status.TODO.name,
                dueDate = now + TimeUnit.DAYS.toMillis(2),
                createdAt = now - TimeUnit.DAYS.toMillis(1)
            ),
            TaskEntity(
                id = SAMPLE_TASK_WEEK_GENERAL,
                title = "Prepare Demo Notes",
                description = "Capture bugs, UI polish ideas, and QA observations before sign-off.",
                category = "General",
                priority = Priority.MEDIUM.value,
                status = Status.IN_PROGRESS.name,
                dueDate = now + TimeUnit.DAYS.toMillis(5),
                createdAt = now - TimeUnit.DAYS.toMillis(2)
            ),
            TaskEntity(
                id = SAMPLE_TASK_LATER_NO_DATE,
                title = "Read Clean Code",
                description = null,
                category = "General",
                priority = Priority.LOW.value,
                status = Status.TODO.name,
                dueDate = null,
                createdAt = now - TimeUnit.DAYS.toMillis(3)
            ),
            TaskEntity(
                id = SAMPLE_TASK_COMPLETED,
                title = "Setup Project Structure",
                description = "Completed state sample for opacity and strike-through treatment.",
                category = "Work",
                priority = Priority.HIGH.value,
                status = Status.COMPLETED.name,
                dueDate = now - TimeUnit.DAYS.toMillis(1),
                createdAt = now - TimeUnit.DAYS.toMillis(5)
            ),
            TaskEntity(
                id = SAMPLE_TASK_OVERDUE,
                title = "Submit Reimbursement Form",
                description = "Overdue item to validate the list grouping and stale-date visibility.",
                category = "Work",
                priority = Priority.MEDIUM.value,
                status = Status.TODO.name,
                dueDate = now - TimeUnit.DAYS.toMillis(2),
                createdAt = now - TimeUnit.DAYS.toMillis(6)
            ),
            TaskEntity(
                id = SAMPLE_TRASH_EXPIRING_SOON,
                title = "Cancelled Project Idea",
                description = "Should show the urgent trash badge state.",
                category = "Work",
                priority = Priority.HIGH.value,
                status = Status.TODO.name,
                dueDate = now - TimeUnit.DAYS.toMillis(1),
                createdAt = now - TimeUnit.DAYS.toMillis(8),
                isDeleted = true,
                deletedAt = now - TimeUnit.HOURS.toMillis(160)
            ),
            TaskEntity(
                id = SAMPLE_TRASH_MID_WINDOW,
                title = "Old Shopping List",
                description = "Should show the standard warning badge state.",
                category = "Shopping",
                priority = Priority.LOW.value,
                status = Status.COMPLETED.name,
                dueDate = now - TimeUnit.DAYS.toMillis(1),
                createdAt = now - TimeUnit.DAYS.toMillis(3),
                isDeleted = true,
                deletedAt = now - TimeUnit.DAYS.toMillis(2)
            ),
            TaskEntity(
                id = SAMPLE_TRASH_JUST_DELETED,
                title = "Mistakenly Deleted Task",
                description = "Fresh trash item for restore affordance checks.",
                category = "Personal",
                priority = Priority.MEDIUM.value,
                status = Status.TODO.name,
                dueDate = now + TimeUnit.DAYS.toMillis(1),
                createdAt = now - TimeUnit.HOURS.toMillis(10),
                isDeleted = true,
                deletedAt = now
            )
        )
    }

    private companion object {
        const val PREFS_NAME = "taskflow_demo_data"
        const val KEY_SAMPLE_VERSION = "sample_version"
        const val SAMPLE_VERSION = 2

        const val SAMPLE_TASK_TODAY_IN_PROGRESS = "sample-task-today-in-progress"
        const val SAMPLE_TASK_TODAY_LONG_TITLE = "sample-task-today-long-title"
        const val SAMPLE_TASK_WEEK_PERSONAL = "sample-task-week-personal"
        const val SAMPLE_TASK_WEEK_GENERAL = "sample-task-week-general"
        const val SAMPLE_TASK_LATER_NO_DATE = "sample-task-later-no-date"
        const val SAMPLE_TASK_COMPLETED = "sample-task-completed"
        const val SAMPLE_TASK_OVERDUE = "sample-task-overdue"
        const val SAMPLE_TRASH_EXPIRING_SOON = "sample-trash-expiring-soon"
        const val SAMPLE_TRASH_MID_WINDOW = "sample-trash-mid-window"
        const val SAMPLE_TRASH_JUST_DELETED = "sample-trash-just-deleted"
    }
}
