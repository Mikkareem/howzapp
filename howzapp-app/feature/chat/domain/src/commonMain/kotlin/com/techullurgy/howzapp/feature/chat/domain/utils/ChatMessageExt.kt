package com.techullurgy.howzapp.feature.chat.domain.utils

import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus

fun ChatMessage.markAsRead(): ChatMessage {
    assert(owner is MessageOwner.Other) {
        "This message cannot be marked as Read"
    }
    return copy(
        owner = (owner as MessageOwner.Other).copy(
            isReadByMe = true
        )
    )
}

fun ChatMessage.isPending(): Boolean {
    return content is Message.PendingMessage && (owner as? MessageOwner.Me)?.msgStatus == MessageStatus.PENDING
}

fun ChatMessage.isUploadPending(): Boolean {
    return isPending() && content is Message.UploadablePendingMessage
            && (content.status is UploadStatus.Started || content.status is UploadStatus.Progress)
}

fun ChatMessage.isDeletable(): Boolean {
    val isRead = when(owner) {
        is MessageOwner.Me -> true
        is MessageOwner.Other -> owner.isReadByMe
    }
    return !isPending() && isRead
}

fun ChatMessage.markStatusPendingToCreated(): ChatMessage {
    assert(owner is MessageOwner.Me && content is Message.PendingMessage && owner.msgStatus == MessageStatus.PENDING) {
        "Cannot Mark Pending to Created"
    }

    val content = when(val content = content as Message.PendingMessage) {
        is Message.NonUploadablePendingMessage -> content.copy(
            isReadyToSync = true
        )
        is Message.UploadablePendingMessage -> {
            assert(content.status is UploadStatus.Success) {
                "Cannot mark the upload as Ready to Sync, because UploadStatus != Success"
            }
            val publicUrl = (content.status as UploadStatus.Success).publicUrl
            content.copy(
                originalMessage = when(val message = content.originalMessage) {
                    is Message.AudioMessage -> message.copy(audioUrl = publicUrl)
                    is Message.DocumentMessage -> message.copy(documentUrl = publicUrl)
                    is Message.ImageMessage -> message.copy(imageUrl = publicUrl)
                    is Message.VideoMessage -> message.copy(videoUrl = publicUrl)
                },
                isReadyToSync = true
            )
        }
    }

    val owner = (owner as MessageOwner.Me).copy(
        msgStatus = MessageStatus.CREATED
    )

    return copy(
        owner = owner,
        content = content
    )
}

fun ChatMessage.upgradeToSuccessUrl(
    publicUrl: String
): ChatMessage {
    require(content is Message.UploadablePendingMessage)

    return copy(
        content = content.copy(
            status = UploadStatus.Success(publicUrl),
            originalMessage = when(content.originalMessage) {
                is Message.AudioMessage -> content.originalMessage.copy(audioUrl = publicUrl)
                is Message.DocumentMessage -> content.originalMessage.copy(documentUrl = publicUrl)
                is Message.ImageMessage -> content.originalMessage.copy(imageUrl = publicUrl)
                is Message.VideoMessage -> content.originalMessage.copy(videoUrl = publicUrl)
            }
        )
    )
}

fun ChatMessage.upgradeToOriginalMessage(): ChatMessage {
    require(content is Message.PendingMessage)

    require(
        when(content) {
            is Message.NonUploadablePendingMessage -> content.isReadyToSync
            is Message.UploadablePendingMessage -> content.isReadyToSync && content.status is UploadStatus.Success
        }
    )

    return copy(
        content = content.originalMessage,
    )
}

fun ChatMessage.markAsReadyToSync(): ChatMessage {
    require(content is Message.PendingMessage)

    require(
        when(content) {
            is Message.NonUploadablePendingMessage -> true
            is Message.UploadablePendingMessage -> content.status is UploadStatus.Success
        }
    )

    val readyToSyncContent = when(content) {
        is Message.NonUploadablePendingMessage -> content.copy(isReadyToSync = true)
        is Message.UploadablePendingMessage -> content.copy(isReadyToSync = true)
    }

    return copy(
        content = content
    )
}