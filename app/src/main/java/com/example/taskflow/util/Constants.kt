package com.example.taskflow.util

import java.util.concurrent.TimeUnit

object Constants {
    // Change this for testing (e.g., TimeUnit.MINUTES.toMillis(1))
    // Default is 7 days as per specs
    val CLEANUP_THRESHOLD_MS = TimeUnit.DAYS.toMillis(7)
}
