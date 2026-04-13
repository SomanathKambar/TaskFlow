package com.example.taskflow.data.local.dao

import androidx.room.*
import com.example.taskflow.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE isDeleted = 0")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isDeleted = 1")
    fun getDeletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :taskId")
    suspend fun softDeleteTask(taskId: String, deletedAt: Long)

    @Query("UPDATE tasks SET isDeleted = 0, deletedAt = null WHERE id = :taskId")
    suspend fun restoreTask(taskId: String)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun hardDeleteTask(taskId: String)

    @Query("DELETE FROM tasks WHERE isDeleted = 1 AND deletedAt <= :threshold")
    suspend fun deleteOldTasks(threshold: Long)
}
