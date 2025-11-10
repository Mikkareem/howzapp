package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

internal actual class PlatformAudioPlayer : AudioPlayer {
    override val activeAudioTrack: StateFlow<AudioTrack?>
        get() = TODO("Not yet implemented")

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun play(id: String, filePath: String, onComplete: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}