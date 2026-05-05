package com.example.rumedcalendar.reminder

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.rumedcalendar.domain.usecase.CheckMissedDosesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MissedCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkMissedDosesUseCase: CheckMissedDosesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            checkMissedDosesUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
