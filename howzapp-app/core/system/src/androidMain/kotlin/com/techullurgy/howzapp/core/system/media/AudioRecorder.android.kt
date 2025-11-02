package com.techullurgy.howzapp.core.system.media

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration.Companion.minutes

internal actual class PlatformAudioRecorder(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : AudioRecorder {

    private val _activeAudioRecordTrack = MutableStateFlow<AudioRecordTrack?>(null)
    override val activeAudioRecordTrack: StateFlow<AudioRecordTrack?> =
        _activeAudioRecordTrack.asStateFlow()

    private var mediaRecorder: MediaRecorder? = null
    private var durationJob: Job? = null

    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(id: String, fileName: String) {
        stop()
        createMediaRecorder().apply {
            val recordingFilePath = context.cacheDir.absolutePath + "/$fileName"
            val fileOutputStream = FileOutputStream(File(recordingFilePath))

            fileOutputStream.use {
                setOutputFile(it.fd)
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setMaxDuration(3.minutes.inWholeMilliseconds.toInt())
                setMaxFileSize(10 * 1024L * 1024L)

                setOnErrorListener { _, what, _ ->
                    when (what) {
                        MediaRecorder.MEDIA_ERROR_SERVER_DIED -> {
                            stop()
                        }

                        MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN -> {
                            stop()
                        }
                    }
                }

                setOnInfoListener { _, what, _ ->
                    when (what) {
                        MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN -> {
                            stop()
                        }

                        MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED -> {
                            stop()
                        }

                        MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_APPROACHING -> {}
                        MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED -> {
                            stop()
                        }
                    }
                }

                prepare()
                start()

                _activeAudioRecordTrack.update {
                    AudioRecordTrack(
                        id = id,
                        recordingPath = recordingFilePath,
                        isRecording = true,
                        duration = 0
                    )
                }

                trackDuration()
            }
        }
    }

    override fun stop() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null
        durationJob?.cancel()
        _activeAudioRecordTrack.update {
            it?.copy(
                isRecording = false
            )
        }
    }

    override fun reset() {
        durationJob?.cancel()
        _activeAudioRecordTrack.update { null }
    }

    private fun trackDuration() {
        durationJob?.cancel()
        durationJob = applicationScope.launch {
            val startTime = System.currentTimeMillis()
            var endTime = System.currentTimeMillis()
            do {
                _activeAudioRecordTrack.update {
                    it?.copy(
                        duration = endTime - startTime
                    )
                }
                delay(400)
                endTime = System.currentTimeMillis()
            } while (activeAudioRecordTrack.value?.isRecording == true)
        }
    }
}