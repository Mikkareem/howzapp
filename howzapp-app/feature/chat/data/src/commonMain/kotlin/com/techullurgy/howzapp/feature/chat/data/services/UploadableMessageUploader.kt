package com.techullurgy.howzapp.feature.chat.data.services

import com.techullurgy.howzapp.core.domain.networking.UploadClient
import com.techullurgy.howzapp.core.domain.util.dropDuplicates
import com.techullurgy.howzapp.core.domain.util.flatten
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.coroutines.coroutineContext

@Single(createdAtStart = true)
internal class UploadableMessageUploader(
    private val chatLocalRepository: ChatLocalRepository,
    private val applicationScope: CoroutineScope,
    private val uploadClient: UploadClient<ByteArray>
) {
    private val triggeredChannel = Channel<ChatMessage>(Channel.UNLIMITED)
    private val cancelledChannel = Channel<ChatMessage>(Channel.UNLIMITED)

    private val cancelledFlow = cancelledChannel.receiveAsFlow()
    private val triggeredFlow = triggeredChannel.receiveAsFlow()

    init {
        chatLocalRepository
            .observeUploadableMessagesThatAreReadyToUpload()
            .flatten()
            .dropDuplicates()
            .onEach { m ->
                triggeredChannel.trySend(m)
            }
            .shareIn(
                scope = applicationScope,
                started = SharingStarted.Eagerly
            )

        chatLocalRepository
            .observeUploadableMessagesThatAreCancelled()
            .flatten()
            .dropDuplicates()
            .onEach { m ->
                cancelledChannel.trySend(m)
            }
            .shareIn(
                scope = applicationScope,
                started = SharingStarted.Eagerly
            )

        triggeredFlow
            .onEach {
                triggerUploadJob(it)
            }
            .launchIn(applicationScope)
    }

    private fun triggerUploadJob(message: ChatMessage) {
        val cancelObservationFlow = flow {
            emit(false)
            cancelledFlow.collect {
                if(it.messageId == message.messageId) {
                    if((it.content as PendingMessage.UploadablePendingMessage).status is UploadStatus.Cancelled) {
                        emit(true)
                    }
                }
            }
        }

        uploadClient.UploadJob(
            parent = applicationScope.coroutineContext,
            uploadKey = message.messageId,
            targetUrl = "",
            source = ByteArray(10),
            sourceLength = 10,
            cancelObservationFlow = cancelObservationFlow,
            onCancelled = { uploadId ->
                chatLocalRepository.deletePendingMessage(uploadId)
            },
            onSuccess = { uploadId, publicUrl ->
                try {
                    chatLocalRepository.updateUploadablePendingMessageAsReady(uploadId, publicUrl)
                } catch (_: Exception) {
                    withContext(NonCancellable) {
                        chatLocalRepository.updateStatusOfUpload(uploadId, UploadStatus.Failed())
                    }
                    coroutineContext.ensureActive()
                }
            },
            onProgress = { uploadId, progress ->
                chatLocalRepository.updateStatusOfUpload(uploadId, UploadStatus.Progress(progress))
            },
            onFailure = { uploadId, _ ->
                chatLocalRepository.updateStatusOfUpload(uploadId, UploadStatus.Failed())
            },
            onStarted = { uploadId ->
                chatLocalRepository.updateStatusOfUpload(uploadId, UploadStatus.Started())
            }
        )
    }
}