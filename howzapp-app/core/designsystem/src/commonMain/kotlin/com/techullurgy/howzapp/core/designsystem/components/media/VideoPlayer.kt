package com.techullurgy.howzapp.core.designsystem.components.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.core.system.media.ContentPlayer

@Composable
fun VideoPlayer(
    player: ContentPlayer?,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlatformVideoPlayer(
        player = player,
        onPause = onPause,
        onResume = onResume,
        onStop = onStop,
        modifier = modifier
    )
}

@Composable
internal expect fun PlatformVideoPlayer(
    player: ContentPlayer?,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier
)