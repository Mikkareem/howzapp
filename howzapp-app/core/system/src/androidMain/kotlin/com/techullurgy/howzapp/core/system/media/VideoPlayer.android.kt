package com.techullurgy.howzapp.core.system.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal actual class PlatformVideoPlayer(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : VideoPlayer {

    private var _activeTrack = MutableStateFlow<VideoTrack?>(null)
    override val activeVideoTrack: StateFlow<VideoTrack?> = _activeTrack.asStateFlow()

    private var exoPlayer: ExoPlayer? = null

    private var durationJob: Job? = null

    override fun play(
        id: String,
        url: String,
        onComplete: () -> Unit
    ) {
        stop()

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            _activeTrack.update {
                VideoTrack(
                    id = id,
                    contentPlayer = ContentPlayer(RestrictedPlayer(this)),
                    totalDuration = 0,
                    durationPlayed = 0,
                    state = PlaybackState.Buffering
                )
            }

            setMediaItem(MediaItem.fromUri(url))
            playWhenReady = true
            prepare()

            _activeTrack.update {
                it?.copy(
                    contentPlayer = ContentPlayer(RestrictedPlayer(this)),
                    durationPlayed = 0,
                    state = PlaybackState.Buffering
                )
            }

            addListener(
                object : Player.Listener {

                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        if (
                            events.containsAny(
                                Player.EVENT_IS_PLAYING_CHANGED,
                                Player.EVENT_IS_LOADING_CHANGED
                            )
                        ) {
                            updateStateOfTrack()
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE,
                            Player.STATE_READY,
                            Player.STATE_BUFFERING -> {
                                updateStateOfTrack()
                            }

                            Player.STATE_ENDED -> {
                                stop()
                                onComplete()
                            }
                        }
                    }
                }
            )
        }
    }

    override fun pause() {
        if (_activeTrack.value?.state !in listOf(PlaybackState.Playing, PlaybackState.Buffering)) {
            return
        }
        exoPlayer?.pause()
    }

    override fun resume() {
        if (_activeTrack.value?.state !in listOf(PlaybackState.Paused, PlaybackState.Stopped)) {
            return
        }
        exoPlayer?.play()
    }

    override fun stop() {
        _activeTrack.update { null }
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun updateStateOfTrack() {
        if (exoPlayer?.isLoading == true) {
            _activeTrack.update {
                it?.copy(
                    state = PlaybackState.Buffering
                )
            }
        } else if (exoPlayer?.isPlaying == true) {
            _activeTrack.update {
                it?.copy(
                    state = PlaybackState.Playing
                )
            }
            trackDuration()
        } else if (exoPlayer?.isPlaying == false) {
            if (exoPlayer?.currentPosition != 0L) {
                _activeTrack.update {
                    it?.copy(
                        state = PlaybackState.Paused
                    )
                }
            } else {
                _activeTrack.update {
                    it?.copy(
                        state = PlaybackState.Stopped
                    )
                }
            }
        }
    }

    private fun trackDuration() {
        durationJob?.cancel()
        durationJob = applicationScope.launch {
            do {
                _activeTrack.update {
                    it?.copy(
                        totalDuration = exoPlayer?.duration ?: 0,
                        durationPlayed = exoPlayer?.currentPosition ?: 0
                    )
                }
                delay(50L)
            } while (exoPlayer?.isPlaying == true)
        }
    }
}

actual data class ContentPlayer(
    val exoPlayer: Player
)

private class RestrictedPlayer(
    player: Player
) : Player by player {
    override fun play() {
        error("Cannot be called play()")
    }

    override fun pause() {
        error("Cannot be called pause()")
    }

    override fun release() {
        error("Cannot be called release()")
    }
}