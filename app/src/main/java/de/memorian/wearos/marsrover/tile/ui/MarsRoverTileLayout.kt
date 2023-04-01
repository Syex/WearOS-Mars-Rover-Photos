package de.memorian.wearos.marsrover.tile.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders.expand
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.LayoutElementBuilders.Image
import androidx.wear.tiles.ResourceBuilders
import com.google.android.horologist.compose.tools.LayoutRootPreview
import com.google.android.horologist.compose.tools.buildDeviceParameters

internal fun marsRoverTileLayout(
    state: MarsRoverTileState,
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
) = Box.Builder()
    .setWidth(expand())
    .setHeight(expand())
    .addContent(
        Image.Builder()
            .setResourceId(MarsRoverTileRenderer.ROVER_IMAGE_ID)
            .setWidth(expand())
            .setHeight(expand())
            .build()
    ).build()

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun MessageTilePreview() {
    val context = LocalContext.current
    val state = MarsRoverTileState("")
    LayoutRootPreview(
        marsRoverTileLayout(
            state,
            context,
            buildDeviceParameters(context.resources)
        )
    ) {
        addIdToImageMapping(
            MarsRoverTileRenderer.ROVER_IMAGE_ID,
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(android.R.drawable.ic_delete)
                        .build()
                ).build()
        )
    }
}
