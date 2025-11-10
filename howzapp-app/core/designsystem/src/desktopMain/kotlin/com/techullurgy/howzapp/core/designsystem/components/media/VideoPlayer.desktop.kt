package com.techullurgy.howzapp.core.designsystem.components.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.core.system.media.ContentPlayer

@Composable
internal actual fun PlatformVideoPlayer(
    player: ContentPlayer?,
    onPlay: (String) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier
) {
}