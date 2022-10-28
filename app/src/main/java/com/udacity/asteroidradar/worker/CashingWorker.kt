package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.repository.AsteroidsRepository

class CashingWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext = appContext, params = params) {
    companion object {
        const val WORKER_NAME = "cashing_worker"
    }

    override suspend fun doWork(): Result {
        val repository = AsteroidsRepository.getInstance(applicationContext)

        return when (repository.refreshAsteroidDatabase()) {
            is NetworkResult.OnSuccess -> Result.success()
            is NetworkResult.OnError -> Result.retry()
        }

    }
}