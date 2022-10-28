package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.worker.CashingWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
//        Timber.plant(Timber.DebugTree())
        delayInit()
    }

    private fun delayInit() {
        scope.launch {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    setRequiresDeviceIdle(true)
                }
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<CashingWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                CashingWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )

        }
    }
}