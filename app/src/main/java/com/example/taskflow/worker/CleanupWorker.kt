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
            val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000L
            val threshold = System.currentTimeMillis() - sevenDaysInMillis
            deleteOldTasksUseCase(threshold)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
