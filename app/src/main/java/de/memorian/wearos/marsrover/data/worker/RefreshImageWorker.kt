package de.memorian.wearos.marsrover.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.memorian.wearos.marsrover.domain.action.RefreshStoredDailyImageAction
import timber.log.Timber

@HiltWorker
class RefreshImageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val refreshStoredDailyImageAction: RefreshStoredDailyImageAction,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.d("Refreshing image now")
        return refreshStoredDailyImageAction.execute(Unit)
            .fold(
                onSuccess = {
                    Result.success()
                },
                onFailure = {
                    Result.failure()
                }
            )
    }
}