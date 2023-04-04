package de.memorian.wearos.marsrover.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.memorian.wearos.marsrover.app.data.persistence.SettingsStore
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalScreenState.ShowPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshIntervalViewModel(
    private val settingsStore: SettingsStore,
    coroutineScope: CoroutineScope?,
) : ViewModel() {

    @Inject
    constructor(settingsStore: SettingsStore) : this(settingsStore, null)

    private val _screenStateFlow =
        MutableStateFlow<RefreshIntervalScreenState>(RefreshIntervalScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val coroutineScope = coroutineScope ?: viewModelScope

    fun loadRefreshInterval() = coroutineScope.launch {
        val refreshInterval = settingsStore.getRefreshInterval()
        _screenStateFlow.emit(ShowPicker(refreshInterval))
    }

    fun onNewIntervalConfirmed(newInterval: Int) = coroutineScope.launch(NonCancellable) {
        settingsStore.storeNewRefreshInterval(newInterval)
    }
}

sealed class RefreshIntervalScreenState {

    object Loading : RefreshIntervalScreenState()

    data class ShowPicker(val initialSelection: Int) : RefreshIntervalScreenState()
}