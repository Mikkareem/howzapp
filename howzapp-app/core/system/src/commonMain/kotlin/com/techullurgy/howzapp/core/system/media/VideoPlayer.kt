package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

interface VideoPlayer {
    val activeVideoTrack: StateFlow<VideoTrack?>

    fun play(id: String, url: String, onComplete: () -> Unit)
    fun pause()
    fun resume()
    fun stop()
}

internal expect class PlatformVideoPlayer : VideoPlayer

expect class ContentPlayer

sealed interface PlaybackState {
    data object Playing : PlaybackState
    data object Paused : PlaybackState
    data object Stopped : PlaybackState
    data object Buffering : PlaybackState
}

data class VideoTrack(
    val id: String,
    val contentPlayer: ContentPlayer?,
    val totalDuration: Long,
    val durationPlayed: Long,
    val state: PlaybackState
)