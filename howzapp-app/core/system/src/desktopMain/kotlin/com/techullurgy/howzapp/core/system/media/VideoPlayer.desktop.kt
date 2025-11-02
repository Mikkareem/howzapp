package com.techullurgy.howzapp.core.system.media

import kotlinx.coroutines.flow.StateFlow

internal actual class PlatformVideoPlayer : VideoPlayer {
    override val activeVideoTrack: StateFlow<VideoTrack?>
        get() = TODO("Not yet implemented")

    override fun play(
        id: String,
        filePath: String,
        onComplete: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}

actual class Player {}