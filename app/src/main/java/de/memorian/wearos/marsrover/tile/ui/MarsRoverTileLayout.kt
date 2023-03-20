package de.memorian.wearos.marsrover.tile.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.google.android.horologist.compose.tools.LayoutRootPreview
import com.google.android.horologist.compose.tools.buildDeviceParameters

internal fun marsRoverTileLayout(
    state: MarsRoverTileState,
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
) = PrimaryLayout.Builder(deviceParameters)
    .setContent(
        LayoutElementBuilders.Text.Builder()
            .setText("Hello Rover!")
            .build()
    ).build()

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun MessageTilePreview() {
    val context = LocalContext.current
    val state = MarsRoverTileState("MessagingRepo.knownContacts")
    LayoutRootPreview(
        marsRoverTileLayout(
            state,
            context,
            buildDeviceParameters(context.resources)
        )
    )
}