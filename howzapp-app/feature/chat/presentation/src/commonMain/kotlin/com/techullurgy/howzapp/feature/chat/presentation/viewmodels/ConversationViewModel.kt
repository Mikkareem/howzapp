package com.techullurgy.howzapp.feature.chat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ConversationViewModel(
    key: ConversationKey
): ViewModel() {

    private val _state = MutableStateFlow(ConversationUiState())
    val state = _state.asStateFlow()
}


internal data class ConversationUiState(

    val title: String = "",
    val subtitle: String = "",
    val profilePicture: String? = null,
    val messageSheets: List<MessageSheet> = messages
)

private val messages = listOf(
    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = false,
        isPictureShowable = true,
        message = Message.TextMessage("Hello People, How are you? Hello People, How are you? Hello People, How are you?")
    ),

    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = true,
        isPictureShowable = true,
        message = Message.TextMessage("Hello People, How are you? Hello People, How are you? Hello People, How are you?")
    ),

    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = false,
        isPictureShowable = true,
        message = Message.TextMessage("Hello People")
    ),

    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = false,
        isPictureShowable = false,
        message = Message.TextMessage("Hello People, How are you? Hello People, How are you? Hello People, How are you?")
    ),

    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = true,
        isPictureShowable = true,
        message = Message.TextMessage("Who is this?")
    ),

    MessageSheet(
        sender = ChatParticipant("",""),
        isCurrentUser = true,
        isPictureShowable = false,
        message = Message.TextMessage("I am fine")
    )
)