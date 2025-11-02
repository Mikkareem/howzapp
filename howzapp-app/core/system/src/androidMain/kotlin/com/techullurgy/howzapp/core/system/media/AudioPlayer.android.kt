package com.techullurgy.howzapp.core.system.media

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream

internal actual class PlatformAudioPlayer(
    private val applicationScope: CoroutineScope
) : AudioPlayer {

    private val _activeTrack = MutableStateFlow<AudioTrack?>(null)
    override val activeAudioTrack: StateFlow<AudioTrack?> = _activeTrack.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    private var durationJob: Job? = null

    override fun pause() {
        if (activeAudioTrack.value?.isPlaying != true) {
            return
        }
        _activeTrack.update {
            it?.copy(
                isPlaying = false
            )
        }
        durationJob?.cancel()
        mediaPlayer?.pause()
    }

    override fun play(id: String, filePath: String, onComplete: () -> Unit) {
        stop()

        mediaPlayer = MediaPlayer().apply {
            val fileInputStream = FileInputStream(File(filePath))
            fileInputStream.use {
                setDataSource(it.fd)
                prepare()
                start()

                _activeTrack.update {
                    AudioTrack(
                        id = id,
                        totalDuration = duration,
                        durationPlayed = 0,
                        isPlaying = true
                    )
                }
                trackDuration()

                setOnCompletionListener {
                    stop()
                    onComplete()
                }
            }
        }
    }

    override fun resume() {
        if (activeAudioTrack.value?.isPlaying != false) {
            return
        }
        _activeTrack.update {
            it?.copy(
                isPlaying = true
            )
        }
        mediaPlayer?.start()
        trackDuration()
    }

    override fun stop() {
        _activeTrack.update {
            it?.copy(
                isPlaying = false,
                durationPlayed = 0
            )
        }
        durationJob?.cancel()
        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }
        mediaPlayer = null
    }

    private fun trackDuration() {
        durationJob?.cancel()
        durationJob = applicationScope.launch {
            do {
                _activeTrack.update {
                    it?.copy(
                        durationPlayed = mediaPlayer?.currentPosition ?: 0
                    )
                }
                delay(10L)
            } while (activeAudioTrack.value?.isPlaying == true && mediaPlayer?.isPlaying == true)
        }
    }
}