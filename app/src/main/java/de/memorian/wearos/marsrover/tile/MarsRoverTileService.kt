package de.memorian.wearos.marsrover.tile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.work.*
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import dagger.hilt.android.AndroidEntryPoint
import de.memorian.wearos.marsrover.app.data.worker.RefreshImageWorker
import de.memorian.wearos.marsrover.app.domain.action.GetRoverImageAction
import de.memorian.wearos.marsrover.tile.ui.MarsRoverTileRenderer
import de.memorian.wearos.marsrover.tile.ui.MarsRoverTileState
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalHorologistApi::class)
@AndroidEntryPoint
class MarsRoverTileService : SuspendingTileService() {

    @Inject
    lateinit var getRoverImageAction: GetRoverImageAction

    private lateinit var renderer: MarsRoverTileRenderer
    private lateinit var imageLoader: ImageLoader

    private var latestTileState = MarsRoverTileState("")

    override fun onCreate() {
        super.onCreate()

        renderer = MarsRoverTileRenderer()
        imageLoader = Coil.imageLoader(this)
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ResourceBuilders.Resources {
        val roverBitmap = fetchRoverBitmap()
        return renderer.produceRequestedResources(
            roverBitmap,
            requestParams
        )
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest,
    ): TileBuilders.Tile {
        latestTileState = getRoverImageAction.execute(Unit)
            .fold(
                onSuccess = {
                    Timber.d("Successfully received an image url: $it")
                    MarsRoverTileState(it)
                },
                onFailure = {
                    Timber.e(it, "Unable to get a rover image")
                    MarsRoverTileState("")
                }
            )
        return renderer.renderTimeline(latestTileState, requestParams)
    }

    private suspend fun fetchRoverBitmap(): Bitmap {
        Timber.d("Fetching bitmap for ${latestTileState.roverImageUrl}")
        val request = ImageRequest.Builder(this)
            .data(latestTileState.roverImageUrl)
            .allowRgb565(true)
            .transformations(CircleCropTransformation())
            .allowHardware(false)
            .build()
        val response = imageLoader.execute(request)
        return (response.drawable as BitmapDrawable).bitmap
    }

    companion object {

        private const val WORK_NAME = "refreshRoverImage"

        fun schedulePeriodicImageRefresh(context: Context, refreshInterval: Int) {
            Timber.d("Scheduling a refresh every $refreshInterval hours")
            val refreshRequest = PeriodicWorkRequestBuilder<RefreshImageWorker>(
                refreshInterval.toLong(),
                TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                refreshRequest
            )
        }
    }
}
