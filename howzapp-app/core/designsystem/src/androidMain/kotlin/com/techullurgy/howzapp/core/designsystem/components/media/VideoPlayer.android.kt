package com.techullurgy.howzapp.core.designsystem.components.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
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
    Box(modifier) {
        if (player != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                PlayerSurface(player.exoPlayer)
                PlayerControls(player, onResume = onResume, onPause = onPause)
            }
        }
    }
}

@Composable
private fun PlayerControls(
    player: ContentPlayer,
    onResume: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playPauseButtonState = rememberPlayPauseButtonState(player.exoPlayer)


}