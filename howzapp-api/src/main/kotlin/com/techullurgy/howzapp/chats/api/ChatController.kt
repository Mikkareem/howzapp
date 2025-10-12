package com.techullurgy.howzapp.chats.api

import com.techullurgy.howzapp.chats.api.dto.NewMessageRequest
import com.techullurgy.howzapp.chats.services.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping("/new")
    fun newMessage(@RequestBody message: NewMessageRequest, auth: Authentication): ResponseEntity<String> {

        val currentUser = auth.name

        chatService.onNewMessage(currentUser, message)
        return ResponseEntity.ok("Sent")
    }
}