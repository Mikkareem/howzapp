package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

interface MediaHandler {
    val activeAudioTrack: StateFlow<AudioTrack?>
    val activeAudioRecordTrack: StateFlow<AudioRecordTrack?>
    val activeVideoTrack: StateFlow<VideoTrack?>
    val isBusy: StateFlow<Boolean>

    fun startAudioRecording(id: String, fileName: String)
    fun stopAudioRecording()
    fun cancelAudioRecording()
    fun resetAudioRecording()

    fun playAudio(id: String, filePath: String, onComplete: () -> Unit)
    fun pauseAudio()
    fun resumeAudio()
    fun stopAudio()

    fun playVideo(id: String, url: String, onComplete: () -> Unit)
    fun pauseVideo()
    fun resumeVideo()
    fun stopVideo()
}