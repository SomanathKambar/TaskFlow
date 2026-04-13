package com.example.taskflow.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskflow.domain.usecase.DeleteOldTasksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val deleteOldTasksUseCase: DeleteOldTasksUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val threshold = System.currentTimeMillis() - com.example.taskflow.util.Constants.CLEANUP_THRESHOLD_MS
            deleteOldTasksUseCase(threshold)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
