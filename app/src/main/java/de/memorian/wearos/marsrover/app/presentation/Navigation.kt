package de.memorian.wearos.marsrover.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import de.memorian.wearos.marsrover.app.presentation.currentimage.CurrentImageViewModel
import de.memorian.wearos.marsrover.app.presentation.currentimage.ViewCurrentImage
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
                },
                onViewCurrentImageClicked = {
                    navController.navigate(Route.VIEW_CURRENT_IMAGE.routeName)
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

        composable(Route.VIEW_CURRENT_IMAGE.routeName) {
            val viewModel = hiltViewModel<CurrentImageViewModel>().apply { loadCurrentImage() }
            ViewCurrentImage(state = viewModel.screenStateFlow.collectAsState())
        }
    }
}

enum class Route(val routeName: String) {

    SETTINGS("settings"),
    PICK_REFRESH_INTERVAL("pickRefreshInterval"),
    VIEW_CURRENT_IMAGE("viewCurrentImage")
}