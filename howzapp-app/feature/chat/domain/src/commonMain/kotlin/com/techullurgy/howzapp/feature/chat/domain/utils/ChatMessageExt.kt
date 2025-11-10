package com.techullurgy.howzapp.feature.chat.domain.utils

//import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
//import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
//import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
//import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
//import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
//
//fun ChatMessage.markAsRead(): ChatMessage {
//    assert(owner is MessageOwner.Other) {
//        "This message cannot be marked as Read"
//    }
//    return copy(
//        owner = (owner as MessageOwner.Other).copy(
//            isReadByMe = true
//        )
//    )
//}
//
//fun ChatMessage.isPending(): Boolean {
//    return content is OriginalMessage.PendingMessage && (owner as? MessageOwner.Me)?.status == MessageStatus.PENDING
//}
//
//fun ChatMessage.isUploadPending(): Boolean {
//    return isPending() && content is OriginalMessage.UploadablePendingMessage
//            && (content.status is UploadStatus.Started || content.status is UploadStatus.Progress)
//}
//
//fun ChatMessage.isDeletable(): Boolean {
//    val isRead = when(owner) {
//        is MessageOwner.Me -> true
//        is MessageOwner.Other -> owner.isReadByMe
//    }
//    return !isPending() && isRead
//}
//
//fun ChatMessage.markStatusPendingToCreated(): ChatMessage {
//    assert(owner is MessageOwner.Me && content is OriginalMessage.PendingMessage && owner.status == MessageStatus.PENDING) {
//        "Cannot Mark Pending to Created"
//    }
//
//    val content = when(val content = content as OriginalMessage.PendingMessage) {
//        is OriginalMessage.NonUploadablePendingMessage -> content.copy(
//            isReadyToSync = true
//        )
//        is OriginalMessage.UploadablePendingMessage -> {
//            assert(content.status is UploadStatus.Success) {
//                "Cannot mark the upload as Ready to Sync, because UploadStatus != Success"
//            }
//            val publicUrl = (content.status as UploadStatus.Success).publicUrl
//            content.copy(
//                originalMessage = when(val message = content.originalMessage) {
//                    is OriginalMessage.AudioMessage -> message.copy(audioUrl = publicUrl)
//                    is OriginalMessage.DocumentMessage -> message.copy(documentUrl = publicUrl)
//                    is OriginalMessage.ImageMessage -> message.copy(imageUrl = publicUrl)
//                    is OriginalMessage.VideoMessage -> message.copy(videoUrl = publicUrl)
//                },
//                isReadyToSync = true
//            )
//        }
//    }
//
//    val owner = (owner as MessageOwner.Me).copy(
//        status = MessageStatus.CREATED
//    )
//
//    return copy(
//        owner = owner,
//        content = content
//    )
//}
//
//fun ChatMessage.upgradeToSuccessUrl(
//    publicUrl: String
//): ChatMessage {
//    require(content is OriginalMessage.UploadablePendingMessage)
//
//    return copy(
//        content = content.copy(
//            status = UploadStatus.Success(publicUrl),
//            originalMessage = when(content.originalMessage) {
//                is OriginalMessage.AudioMessage -> content.originalMessage.copy(audioUrl = publicUrl)
//                is OriginalMessage.DocumentMessage -> content.originalMessage.copy(documentUrl = publicUrl)
//                is OriginalMessage.ImageMessage -> content.originalMessage.copy(imageUrl = publicUrl)
//                is OriginalMessage.VideoMessage -> content.originalMessage.copy(videoUrl = publicUrl)
//            }
//        )
//    )
//}
//
//fun ChatMessage.upgradeToOriginalMessage(): ChatMessage {
//    require(content is OriginalMessage.PendingMessage)
//
//    require(
//        when(content) {
//            is OriginalMessage.NonUploadablePendingMessage -> content.isReadyToSync
//            is OriginalMessage.UploadablePendingMessage -> content.isReadyToSync && content.status is UploadStatus.Success
//        }
//    )
//
//    return copy(
//        content = content.originalMessage,
//    )
//}
//
//fun ChatMessage.markAsReadyToSync(): ChatMessage {
//    require(content is OriginalMessage.PendingMessage)
//
//    require(
//        when(content) {
//            is OriginalMessage.NonUploadablePendingMessage -> true
//            is OriginalMessage.UploadablePendingMessage -> content.status is UploadStatus.Success
//        }
//    )
//
//    val readyToSyncContent = when(content) {
//        is OriginalMessage.NonUploadablePendingMessage -> content.copy(isReadyToSync = true)
//        is OriginalMessage.UploadablePendingMessage -> content.copy(isReadyToSync = true)
//    }
//
//    return copy(
//        content = content
//    )
//}