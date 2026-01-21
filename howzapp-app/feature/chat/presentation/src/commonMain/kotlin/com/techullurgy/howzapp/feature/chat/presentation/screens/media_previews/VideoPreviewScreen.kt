package com.techullurgy.howzapp.feature.chat.presentation.screens.media_previews

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.core.designsystem.components.media.VideoPlayer
import com.techullurgy.howzapp.core.system.media.ContentPlayer
import com.techullurgy.howzapp.core.system.media.MediaHandler
import com.techullurgy.howzapp.core.system.media.PlaybackState
import com.techullurgy.howzapp.feature.chat.api.navigation.IVideoPreviewScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.parameter.parametersOf

@Factory(binds = [IVideoPreviewScreen::class])
internal class DefaultIVideoPreviewScreen : IVideoPreviewScreen {
    @Composable
    override fun invoke(listenId: String, url: String) {
        VideoPreviewScreen(listenId = listenId, url = url)
    }
}

@Composable
private fun VideoPreviewScreen(
    listenId: String,
    url: String,
) {
    val viewModel = koinViewModel<VideoPreviewViewModel> {
        parametersOf(listenId, url)
    }

    val state by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = state.contentPlayer
        ) { player ->
            if (player != null) {
                VideoPlayer(
                    player = state.contentPlayer,
                    onPause = { viewModel.pause() },
                    onResume = { viewModel.resume() },
                    onStop = { viewModel.stop() }
                )
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@KoinViewModel
internal class VideoPreviewViewModel(
    private val mediaHandler: MediaHandler,
    private val videoListenId: String,
    private val videoUrl: String
) : ViewModel() {

    private val stopNotifyChannel = Channel<Unit>()
    val stopNotificationFlow = stopNotifyChannel.receiveAsFlow()

    private val _state = MutableStateFlow(VideoPreviewUiState())
    val state = _state
        .onStart {
            observeVideoPlayer()
            mediaHandler.playVideo(
                videoListenId,
                videoUrl,
                onComplete = {}
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    fun resume() {
        mediaHandler.resumeVideo()
    }

    fun pause() {
        mediaHandler.pauseVideo()
    }

    fun stop() {
        mediaHandler.stopVideo()
        stopNotifyChannel.trySend(Unit)
    }

    private fun observeVideoPlayer() {
        mediaHandler.activeVideoTrack
            .transform { track ->
                if (track == null || track.id != videoListenId) {
                    emit(null)
                } else {
                    emit(track)
                }
            }
            .onEach { track ->
                if (track == null) {
                    _state.update {
                        it.copy(
                            contentPlayer = null,
                            isPlaying = false,
                            durationPlayed = 0,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            contentPlayer = track.contentPlayer,
                            duration = track.totalDuration.toInt(),
                            durationPlayed = track.durationPlayed.toInt()
                        )
                    }
                    _state.update {
                        when (track.state) {
                            PlaybackState.Buffering -> it.copy(
                                isLoading = true,
                                isPlaying = false
                            )

                            PlaybackState.Paused -> it.copy(
                                isPlaying = false,
                                isLoading = false
                            )

                            PlaybackState.Playing -> it.copy(
                                isPlaying = true,
                                isLoading = false
                            )

                            PlaybackState.Stopped -> {
                                stopNotifyChannel.trySend(Unit)
                                it.copy(
                                    isPlaying = false,
                                    isLoading = false,
                                    durationPlayed = 0
                                )
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}

internal data class VideoPreviewUiState(
    val contentPlayer: ContentPlayer? = null,
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val duration: Int = 0,
    val durationPlayed: Int = 0
)