package com.techullurgy.howzapp.core.data.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

abstract class AppUploadClient<T> {
    protected abstract fun uploadMedia(
        source: T,
        sourceLength: Long,
        targetUrl: String,
        requestHeaders: Map<String, String> = emptyMap()
    ): Flow<NetworkUploadStatus>

    @OptIn(ExperimentalCoroutinesApi::class)
    inner class UploadJob(
        parent: CoroutineContext,
        uploadKey: String,
        targetUrl: String,
        source: T,
        sourceLength: Long,
        cancelObservationFlow: Flow<Boolean>,
        onCancelled: suspend (String) -> Unit,
        onStarted: suspend (String) -> Unit,
        onSuccess: suspend (String, String) -> Unit,
        onFailure: suspend (String, Throwable?) -> Unit,
        onProgress: suspend (String, Double) -> Unit
    ) {
        private var job: Job? = null

        init {
            job = SupervisorJob(parent[Job.Key])
            cancelObservationFlow
                .distinctUntilChanged()
                .flatMapLatest<Boolean, Unit> { isCancelled ->
                    if(isCancelled) {
                        onCancelled(uploadKey)
                        cancel()
                        emptyFlow()
                    } else {
                        uploadMedia(source = source, sourceLength = sourceLength, targetUrl = targetUrl)
                            .onEach {
                                when(it) {
                                    NetworkUploadStatus.Cancelled -> onCancelled(uploadKey)
                                    NetworkUploadStatus.Failed -> onFailure(uploadKey,RuntimeException("Failed"))
                                    is NetworkUploadStatus.Progress -> onProgress(uploadKey, it.progress)
                                    NetworkUploadStatus.Started -> onStarted(uploadKey)
                                    is NetworkUploadStatus.Success -> onSuccess(uploadKey, it.url)
                                }
                            }
                            .onCompletion {
                                it?.let {
                                    onFailure(uploadKey, it)
                                }
                                cancel()
                            }
                            .map {  }
                    }
                }
                .launchIn(CoroutineScope(parent + job!!))
                .invokeOnCompletion { cancel() }
        }

        fun cancel() {
            job?.cancel()
            job = null
        }
    }

    sealed interface NetworkUploadStatus {
        data object Failed : NetworkUploadStatus
        data object Cancelled : NetworkUploadStatus
        data object Started : NetworkUploadStatus
        data class Progress(val progress: Double) : NetworkUploadStatus
        data class Success(val url: String) : NetworkUploadStatus
    }
}