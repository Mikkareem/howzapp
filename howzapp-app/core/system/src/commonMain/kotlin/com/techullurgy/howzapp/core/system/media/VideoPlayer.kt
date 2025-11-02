package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

interface VideoPlayer {
    val activeVideoTrack: StateFlow<VideoTrack?>

    fun play(id: String, filePath: String, onComplete: () -> Unit)
    fun pause()
    fun resume()
    fun stop()
}

internal expect class PlatformVdieoPlayer : VideoPlayer

data class VideoTrack(
    val id: String,
    val totalDuration: Int,
    val durationPlayed: Int,
    val isPlaying: Boolean
)