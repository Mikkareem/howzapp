package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single

@Single(binds = [MediaHandler::class])
internal class SynchronousMediaHandler(
    private val audioPlayer: AudioPlayer,
    private val audioRecorder: AudioRecorder,
    private val videoPlayer: VideoPlayer,
    private val scope: CoroutineScope
) : MediaHandler {
    private val currentState = MutableStateFlow<MediaState>(MediaState.MediaIdle)

    override val activeAudioTrack = audioPlayer.activeAudioTrack
    override val activeAudioRecordTrack = audioRecorder.activeAudioRecordTrack
    override val activeVideoTrack: StateFlow<VideoTrack?> = videoPlayer.activeVideoTrack

    override val isBusy = combine(
        activeAudioTrack,
        activeAudioRecordTrack,
        activeVideoTrack
    ) { audio, audioRecord, video ->
        audio != null || audioRecord != null || video != null
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    override fun startAudioRecording(id: String, fileName: String) {
        stopAll()
        currentState.value = MediaState.MediaAudioRecorder
        audioRecorder.start(id, fileName)
    }

    override fun stopAudioRecording() {
        if (currentState.value != MediaState.MediaAudioRecorder) return
        currentState.value = MediaState.MediaIdle
        audioRecorder.stop()
    }

    override fun cancelAudioRecording() {
        if (currentState.value != MediaState.MediaAudioRecorder) return
        currentState.value = MediaState.MediaIdle
        audioRecorder.cancel()
    }

    override fun resetAudioRecording() {
        // Checking currentState needed?
        audioRecorder.reset()
    }

    override fun playAudio(id: String, filePath: String, onComplete: () -> Unit) {
        stopAll()
        currentState.value = MediaState.MediaAudioPlayer
        audioPlayer.play(id, filePath, onComplete)
    }

    override fun pauseAudio() {
        if (currentState.value != MediaState.MediaAudioPlayer) return
        audioPlayer.pause()
    }

    override fun resumeAudio() {
        if (currentState.value != MediaState.MediaAudioPlayer) return
        audioPlayer.resume()
    }

    override fun stopAudio() {
        if (currentState.value != MediaState.MediaAudioPlayer) return
        audioPlayer.stop()
    }

    override fun pauseVideo() {
        if (currentState.value != MediaState.MediaVideoPlayer) return
        videoPlayer.pause()
    }

    override fun playVideo(id: String, url: String, onComplete: () -> Unit) {
        stopAll()
        currentState.value = MediaState.MediaVideoPlayer
        videoPlayer.play(id, url, onComplete)
    }

    override fun resumeVideo() {
        if (currentState.value != MediaState.MediaVideoPlayer) return
        videoPlayer.resume()
    }

    override fun stopVideo() {
        if (currentState.value != MediaState.MediaVideoPlayer) return
        videoPlayer.stop()
    }

    private fun stopAll() {
        cancelAudioRecording()
        stopAudio()
        stopVideo()
    }
}

private sealed interface MediaState {
    data object MediaIdle : MediaState
    data object MediaAudioRecorder : MediaState
    data object MediaAudioPlayer : MediaState
    data object MediaVideoPlayer : MediaState
}