//package com.udacity.asteroidradar.worker
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.udacity.asteroidradar.data.local.AsteroidDatabase
//import com.udacity.asteroidradar.data.remote.AsteroidNewWsApi
//import com.udacity.asteroidradar.repository.AsteroidsRepository
//
//class CashingWorker(appContext: Context, params: WorkerParameters) :
//    CoroutineWorker(appContext = appContext, params = params) {
//    override suspend fun doWork(): Result {
//        val repository = AsteroidsRepository(
//            AsteroidNewWsApi.getInstance(),
//            AsteroidDatabase.getInstance(context = applicationContext),
//            context = applicationContext
//        )
//
//       when(repository.refreshAsteroidDatabase())
//
//
//    }
//}