package com.techullurgy.howzapp.core.system.di

import com.techullurgy.howzapp.core.system.media.AudioPlayer
import com.techullurgy.howzapp.core.system.media.AudioRecorder
import com.techullurgy.howzapp.core.system.media.VideoPlayer
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal expect class PlatformCoreSystemModule {
    @Single
    fun provideAudioPlayer(scope: Scope): AudioPlayer

    @Single
    fun provideAudioRecorder(scope: Scope): AudioRecorder

    @Single
    fun provideVideoPlayer(scope: Scope): VideoPlayer
}

@Module(includes = [PlatformCoreSystemModule::class])
class CoreSystemModule