package de.memorian.wearos.marsrover.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalPicker
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalViewModel
import de.memorian.wearos.marsrover.app.presentation.settings.SettingsScreen

@Composable
fun NavHost() {
    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Route.SETTINGS.routeName
    ) {
        composable(Route.SETTINGS.routeName) {
            SettingsScreen(
                onPickRefreshIntervalClicked = {
                    navController.navigate(Route.PICK_REFRESH_INTERVAL.routeName)
                }
            )
        }

        composable(Route.PICK_REFRESH_INTERVAL.routeName) {
            val viewModel =
                hiltViewModel<RefreshIntervalViewModel>().apply { loadRefreshInterval() }
            RefreshIntervalPicker(
                state = viewModel.screenStateFlow.collectAsState(),
                onNewIntervalConfirmed = {
                    viewModel.onNewIntervalConfirmed(it)
                    navController.navigateUp()
                }
            )
        }
    }
}

enum class Route(val routeName: String) {

    SETTINGS("settings"),
    PICK_REFRESH_INTERVAL("pickRefreshInterval")
}