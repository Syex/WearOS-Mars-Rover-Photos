package de.memorian.wearos.marsrover.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import de.memorian.wearos.marsrover.R
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalScreenState.Loading
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalScreenState.ShowPicker
import de.memorian.wearos.marsrover.app.presentation.theme.MarsRoverTheme

private const val MIN_INTERVAL = 1
private const val MAX_INTERVAL = 24

private val pickableIntervals = buildList {
    (MIN_INTERVAL..MAX_INTERVAL).forEach { add(it) }
}

@Composable
fun RefreshIntervalPicker(
    state: State<RefreshIntervalScreenState>,
    onNewIntervalConfirmed: (Int) -> Unit,
) {
    val _screenState by state

    when (val screenState = _screenState) {
        Loading -> {}
        is ShowPicker -> IntervalPicker(
            screenState.initialSelection,
            onNewIntervalConfirmed
        )
    }
}

@Composable
private fun IntervalPicker(
    initialSelection: Int,
    onNewIntervalConfirmed: (Int) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        val pickerState = rememberPickerState(
            initialNumberOfOptions = pickableIntervals.size,
            initiallySelectedOption = pickableIntervals.indexOf(initialSelection),
            repeatItems = false
        )
        Picker(
            modifier = Modifier.align(Alignment.Center),
            state = pickerState,
            contentDescription = null,
        ) {
            Text(text = pickableIntervals[it].toString(), fontSize = 36.sp)
        }

        Text(
            text = stringResource(R.string.refresh_interval_label),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .background(MaterialTheme.colors.background),
            fontWeight = FontWeight.Bold,
        )

        CompactButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { onNewIntervalConfirmed(pickableIntervals[pickerState.selectedOption]) }
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun RefreshIntervalPickerPreview() {
    MarsRoverTheme {
        RefreshIntervalPicker(state = object : State<RefreshIntervalScreenState> {
            override val value: RefreshIntervalScreenState
                get() = ShowPicker(initialSelection = 12)
        }, onNewIntervalConfirmed = {})
    }
}