package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

internal interface AudioRecorder {
    val activeAudioRecordTrack: StateFlow<AudioRecordTrack?>

    fun start(id: String, fileName: String)
    fun stop()

    fun reset()
}

internal expect class PlatformAudioRecorder : AudioRecorder

data class AudioRecordTrack(
    val id: String,
    val isRecording: Boolean,
    val recordingPath: String,
    val duration: Long,
)