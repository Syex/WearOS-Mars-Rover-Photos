package de.memorian.wearos.marsrover.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshIntervalViewModel(
    coroutineScope: CoroutineScope?,
) : ViewModel() {

    @Inject
    constructor() : this(null)

    private val _screenStateFlow =
        MutableStateFlow<RefreshIntervalScreenState>(RefreshIntervalScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val coroutineScope = coroutineScope ?: viewModelScope

    init {
        _screenStateFlow.tryEmit(RefreshIntervalScreenState.ShowPicker(8))
    }

    fun onNewIntervalConfirmed(newInterval: Int) = coroutineScope.launch {

    }
}

sealed class RefreshIntervalScreenState {

    object Loading : RefreshIntervalScreenState()

    data class ShowPicker(val initialSelection: Int) : RefreshIntervalScreenState()
}