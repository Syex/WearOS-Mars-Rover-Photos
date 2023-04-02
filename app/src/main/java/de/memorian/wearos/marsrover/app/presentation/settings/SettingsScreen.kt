package de.memorian.wearos.marsrover.app.presentation.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.*
import de.memorian.wearos.marsrover.R
import de.memorian.wearos.marsrover.app.presentation.theme.MarsRoverTheme

@Composable
fun SettingsScreen(
    onPickRefreshIntervalClicked: () -> Unit,
) {
    ScalingLazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            ListHeader {
                Text(text = "Mars Rover settings")
            }
        }
        item {
            Chip(
                label = { Text(text = stringResource(id = R.string.title_refresh_interval)) },
                onClick = { onPickRefreshIntervalClicked() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_schedule),
                        contentDescription = null,
                        modifier = Modifier
                            .size(ChipDefaults.LargeIconSize)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MarsRoverTheme {
        SettingsScreen(
            onPickRefreshIntervalClicked = {}
        )
    }
}