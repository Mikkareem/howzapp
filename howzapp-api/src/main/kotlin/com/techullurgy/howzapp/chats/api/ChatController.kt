package com.techullurgy.howzapp.chats.api

import com.techullurgy.howzapp.chats.api.dto.LoadChatMessagesResponse
import com.techullurgy.howzapp.chats.api.dto.MessageReceiptRequest
import com.techullurgy.howzapp.chats.api.dto.NewMessageRequest
import com.techullurgy.howzapp.chats.api.dto.NewMessageResponse
import com.techullurgy.howzapp.chats.api.dto.SyncResponse
import com.techullurgy.howzapp.chats.services.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping("/message/new")
    fun newMessage(
        @RequestBody request: NewMessageRequest,
        auth: Authentication
    ): ResponseEntity<NewMessageResponse> {
        val currentUser = auth.name

        val updatedMessageId = chatService.onNewMessage(
            from = currentUser,
            chatId = request.chatId,
            message = request.message
        )

        return ResponseEntity.ok(
            NewMessageResponse(
                localMessageId = request.localMessageId,
                serverMessageId = updatedMessageId
            )
        )
    }

    @GetMapping("/sync")
    fun sync(
        @RequestParam("lastSyncTimestamp") lastSyncTimestamp: Long,
        auth: Authentication
    ): ResponseEntity<SyncResponse> {
        val currentUser = auth.name

        val chats = chatService.loadNewMessagesForUser(
            userId = currentUser,
            after = Instant.ofEpochMilli(lastSyncTimestamp)
        )

        return ResponseEntity.ok(
            SyncResponse(chats, Instant.now().toEpochMilli())
        )
    }

    @GetMapping("/load")
    fun loadMessageFromChat(
        @RequestParam("chatId") chatId: String,
        @RequestParam("beforeMessage") beforeMessage: String,
        auth: Authentication
    ): ResponseEntity<LoadChatMessagesResponse> {
        val currentUser = auth.name

        val messages = chatService.loadMessagesFromChatBefore(
            userId = currentUser,
            chatId = chatId,
            lastMessage = beforeMessage
        )

        return ResponseEntity.ok(
            LoadChatMessagesResponse(
                hasPreviousAvailable = messages.size == 20,
                messages = messages
            )
        )
    }

    @PutMapping("/receipt")
    fun receipt(
        @RequestBody request: MessageReceiptRequest,
        auth: Authentication
    ): ResponseEntity<Unit> {
        val currentUser = auth.name

        chatService.updateMessageReceipt(
            participantId = currentUser,
            messageId = request.messageId,
            receipt = request.receipt,
        )

        return ResponseEntity.ok(Unit)
    }
}