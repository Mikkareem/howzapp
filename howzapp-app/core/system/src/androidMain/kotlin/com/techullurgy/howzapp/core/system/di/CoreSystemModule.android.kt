package com.techullurgy.howzapp.core.system.di

import com.techullurgy.howzapp.core.system.media.AudioPlayer
import com.techullurgy.howzapp.core.system.media.AudioRecorder
import com.techullurgy.howzapp.core.system.media.PlatformAudioPlayer
import com.techullurgy.howzapp.core.system.media.PlatformAudioRecorder
import com.techullurgy.howzapp.core.system.media.PlatformVideoPlayer
import com.techullurgy.howzapp.core.system.media.VideoPlayer
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class PlatformCoreSystemModule {
    @Single
    actual fun provideAudioPlayer(scope: Scope): AudioPlayer {
        return PlatformAudioPlayer(scope.get())
    }

    @Single
    actual fun provideAudioRecorder(scope: Scope): AudioRecorder {
        return PlatformAudioRecorder(scope.get(), scope.get())
    }

    @Single
    actual fun provideVideoPlayer(scope: Scope): VideoPlayer {
        return PlatformVideoPlayer(scope.get(), scope.get())
    }
}