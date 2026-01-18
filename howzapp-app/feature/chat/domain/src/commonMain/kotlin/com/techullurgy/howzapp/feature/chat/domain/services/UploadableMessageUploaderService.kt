package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.networking.UploadClient
import com.techullurgy.howzapp.core.domain.util.dropDuplicates
import com.techullurgy.howzapp.core.domain.util.flatten
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.system.FileReader
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
internal class UploadableMessageUploaderService(
    private val chatLocalRepository: ChatLocalRepository,
    private val applicationScope: CoroutineScope,
    private val uploadClient: UploadClient<ByteArray>,
    private val fileReader: FileReader,
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

    private suspend fun triggerUploadJob(message: ChatMessage) {

        val localFileUrl = when(val originalMessage = (message.content as PendingMessage.UploadablePendingMessage).originalMessage) {
            is OriginalMessage.AudioMessage -> originalMessage.audioUrl
            is OriginalMessage.DocumentMessage -> originalMessage.documentUrl
            is OriginalMessage.ImageMessage -> originalMessage.imageUrl
            is OriginalMessage.VideoMessage -> originalMessage.videoUrl
        }

        val uploadableBytes = try {
            fileReader.getFileBytesFromUrl(localFileUrl)
        } catch (_: Exception) {
            withContext(NonCancellable) {
                chatLocalRepository.updateStatusOfUpload(message.messageId, UploadStatus.Failed())
            }
            coroutineContext.ensureActive()
            return
        }

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
            targetUrl = "/api/upload",
            source = uploadableBytes,
            sourceLength = uploadableBytes.size.toLong(),
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