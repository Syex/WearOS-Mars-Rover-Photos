package de.memorian.wearos.marsrover.tile.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ResourceBuilders
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import com.google.android.horologist.tiles.images.toImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer

@OptIn(ExperimentalHorologistTilesApi::class)
class MarsRoverTileRenderer(context: Context) :
    SingleTileLayoutRenderer<MarsRoverTileState, Bitmap>(context) {

    override fun renderTile(
        state: MarsRoverTileState,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
    ): LayoutElementBuilders.LayoutElement {
        return marsRoverTileLayout(state, context, deviceParameters)
    }

    override fun ResourceBuilders.Resources.Builder.produceRequestedResources(
        resourceState: Bitmap,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
        addIdToImageMapping(
            ROVER_IMAGE_ID,
            resourceState.toImageResource()
        )
    }

    companion object {

        const val ROVER_IMAGE_ID = "roverImageId"
    }
}