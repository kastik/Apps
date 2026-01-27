package com.kastik.apps.core.data.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.service.TokenRefreshScheduler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TokenRefreshSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenRefreshScheduler {

    companion object {
        const val WORK_NAME = "TOKEN_REFRESH_WORK"
    }

    override fun scheduleRefresh() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<TokenRefreshWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = request
        )
    }
}

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val authenticationRepository: AuthenticationRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {

            authenticationRepository.refreshAboardToken()
            return Result.success()

        } catch (e: HttpException) {
            if (e.code() == 401) {
                authenticationRepository.clearAuthenticationData()
                return Result.failure()
            }

            return Result.retry()

        } catch (e: Exception) {

            return Result.retry()

        }
    }
}

