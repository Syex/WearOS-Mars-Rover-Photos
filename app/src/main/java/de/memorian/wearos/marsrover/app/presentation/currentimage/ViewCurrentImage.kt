package de.memorian.wearos.marsrover.app.presentation.currentimage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.CircularProgressIndicator
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ViewCurrentImage(
    state: State<CurrentImageScreenState>,
) {
    val _screenState by state

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val screenState = _screenState) {
            CurrentImageScreenState.Loading -> CircularProgressIndicator()
            is CurrentImageScreenState.ShowImage -> AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(screenState.image)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }
}