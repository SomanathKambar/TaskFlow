package com.example.taskflow

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskflow.worker.CleanupWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TaskFlowApplication : Application(), Configuration.Provider {


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleCleanupWorker()
    }

    private fun scheduleCleanupWorker() {
        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(
            24, TimeUnit.HOURS
        ).setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "cleanup_tasks",
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest
        )
    }
}
