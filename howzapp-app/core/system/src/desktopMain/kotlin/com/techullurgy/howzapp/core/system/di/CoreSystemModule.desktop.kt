package com.techullurgy.howzapp.core.system.di

import com.techullurgy.howzapp.core.system.media.AudioPlayer
import com.techullurgy.howzapp.core.system.media.AudioRecorder
import com.techullurgy.howzapp.core.system.media.PlatformAudioPlayer
import com.techullurgy.howzapp.core.system.media.PlatformAudioRecorder
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class PlatformCoreSystemModule {
    @Single
    actual fun provideAudioPlayer(scope: Scope): AudioPlayer {
        return PlatformAudioPlayer()
    }

    @Single
    actual fun provideAudioRecorder(scope: Scope): AudioRecorder {
        return PlatformAudioRecorder()
    }
}