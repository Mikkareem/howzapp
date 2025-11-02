package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

internal interface AudioPlayer {
    val activeAudioTrack: StateFlow<AudioTrack?>

    fun play(id: String, filePath: String, onComplete: () -> Unit)
    fun pause()
    fun resume()
    fun stop()
}

internal expect class PlatformAudioPlayer : AudioPlayer

data class AudioTrack(
    val id: String,
    val totalDuration: Int,
    val durationPlayed: Int,
    val isPlaying: Boolean
)