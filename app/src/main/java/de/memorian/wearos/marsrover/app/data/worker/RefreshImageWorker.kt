package de.memorian.wearos.marsrover.app.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.wear.tiles.TileService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.memorian.wearos.marsrover.app.domain.action.RefreshStoredDailyImageAction
import de.memorian.wearos.marsrover.tile.MarsRoverTileService
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
                    TileService.getUpdater(applicationContext)
                        .requestUpdate(MarsRoverTileService::class.java)
                    Result.success()
                },
                onFailure = {
                    Result.failure()
                }
            )
    }
}