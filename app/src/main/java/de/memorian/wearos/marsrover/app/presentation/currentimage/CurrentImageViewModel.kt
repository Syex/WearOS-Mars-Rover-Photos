package de.memorian.wearos.marsrover.app.presentation.currentimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.memorian.wearos.marsrover.app.domain.action.GetRoverImageAction
import de.memorian.wearos.marsrover.app.presentation.currentimage.CurrentImageScreenState.ShowImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentImageViewModel(
    private val getRoverImageAction: GetRoverImageAction,
    coroutineScope: CoroutineScope?,
) : ViewModel() {

    @Inject
    constructor(getRoverImageAction: GetRoverImageAction) : this(getRoverImageAction, null)

    private val _screenStateFlow =
        MutableStateFlow<CurrentImageScreenState>(CurrentImageScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val coroutineScope = coroutineScope ?: viewModelScope

    fun loadCurrentImage() = coroutineScope.launch {
        getRoverImageAction.execute(Unit)
            .onSuccess {
                _screenStateFlow.emit(ShowImage(it))
            }
    }
}

sealed class CurrentImageScreenState {

    object Loading : CurrentImageScreenState()

    data class ShowImage(val image: String) : CurrentImageScreenState()
}