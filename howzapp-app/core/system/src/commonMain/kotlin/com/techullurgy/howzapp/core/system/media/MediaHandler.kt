package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

interface MediaHandler {
    val activeAudioTrack: StateFlow<AudioTrack?>
    val activeAudioRecordTrack: StateFlow<AudioRecordTrack?>
    val isBusy: StateFlow<Boolean>

    fun startAudioRecording(id: String, fileName: String)
    fun stopAudioRecording()
    fun resetAudioRecording()

    fun playAudio(id: String, filePath: String, onComplete: () -> Unit)
    fun pauseAudio()
    fun resumeAudio()
    fun stopAudio()
}