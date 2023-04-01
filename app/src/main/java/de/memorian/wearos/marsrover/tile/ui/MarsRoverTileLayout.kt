package de.memorian.wearos.marsrover.tile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tiles.DimensionBuilders.expand
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.LayoutElementBuilders.Image
import androidx.wear.tiles.ResourceBuilders
import com.google.android.horologist.compose.tools.LayoutRootPreview

internal fun marsRoverTileLayout() = Box.Builder()
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
    LayoutRootPreview(
        marsRoverTileLayout()
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
