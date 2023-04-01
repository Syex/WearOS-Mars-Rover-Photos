package de.memorian.wearos.marsrover.tile.ui

import android.graphics.Bitmap
import androidx.wear.tiles.*
import androidx.wear.tiles.TimelineBuilders.*
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import com.google.android.horologist.tiles.images.toImageResource
import com.google.android.horologist.tiles.render.TileLayoutRenderer
import java.util.*

@OptIn(ExperimentalHorologistTilesApi::class)
class MarsRoverTileRenderer : TileLayoutRenderer<MarsRoverTileState, Bitmap> {

    override fun renderTimeline(
        state: MarsRoverTileState,
        requestParams: RequestBuilders.TileRequest,
    ): TileBuilders.Tile {
        val rootLayout = marsRoverTileLayout()

        val singleTileTimeline = Timeline.Builder()
            .addTimelineEntry(
                TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(rootLayout)
                            .build()
                    ).build()
            ).build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(state.hashCode().toString())
            .setTimeline(singleTileTimeline)
            .build()
    }

    override fun produceRequestedResources(
        resourceState: Bitmap,
        requestParams: RequestBuilders.ResourcesRequest,
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(requestParams.version)
            .addIdToImageMapping(
                ROVER_IMAGE_ID,
                resourceState.toImageResource()
            ).build()
    }

    companion object {

        const val ROVER_IMAGE_ID = "roverImageId"
    }
}