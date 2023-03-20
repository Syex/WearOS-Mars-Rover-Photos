package de.memorian.wearos.marsrover.tile

import android.graphics.Bitmap
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import com.google.android.horologist.tiles.SuspendingTileService
import de.memorian.wearos.marsrover.tile.ui.MarsRoverTileRenderer
import de.memorian.wearos.marsrover.tile.ui.MarsRoverTileState

@OptIn(ExperimentalHorologistTilesApi::class)
class MarsRoverTileService : SuspendingTileService() {

    private lateinit var renderer: MarsRoverTileRenderer

    override fun onCreate() {
        super.onCreate()

        renderer = MarsRoverTileRenderer(this)
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ResourceBuilders.Resources {
        return renderer.produceRequestedResources(
            Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888),
            requestParams
        )
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest,
    ): TileBuilders.Tile {
        val state = MarsRoverTileState("")
        return renderer.renderTimeline(state, requestParams)
    }
}