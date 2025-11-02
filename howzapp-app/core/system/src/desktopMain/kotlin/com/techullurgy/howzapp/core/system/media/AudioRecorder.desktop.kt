package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

internal actual class PlatformAudioRecorder : AudioRecorder {
    override val activeAudioRecordTrack: StateFlow<AudioRecordTrack?>
        get() = TODO("Not yet implemented")

    override fun start(id: String, fileName: String) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}