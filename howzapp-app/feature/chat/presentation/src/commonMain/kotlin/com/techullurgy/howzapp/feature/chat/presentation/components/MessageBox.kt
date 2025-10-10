package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.presentation.components.layouts.MessageViewLayout
import com.techullurgy.howzapp.feature.chat.presentation.components.layouts.MessageViewUi

@Composable
internal fun MessageBox(
    sheet: MessageSheet,
    modifier: Modifier = Modifier
) {
    val direction = if(sheet.isCurrentUser) LayoutDirection.Rtl else LayoutDirection.Ltr

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = when(direction) {
            LayoutDirection.Ltr -> Arrangement.Start
            LayoutDirection.Rtl -> Arrangement.End
        }
    ) {

        val view = if(sheet.isPictureShowable) {
            MessageViewUi.Anchored(
                direction = direction
            ) {
                Box(Modifier.background(shape = CircleShape, color = Color.Magenta))
            }
        } else {
            MessageViewUi.NonAnchored(
                direction = direction
            )
        }

        MessageViewLayout(
            view = view,
            message = sheet.message,
            owner = sheet.messageOwner
        )
    }
}

internal data class MessageSheet(
    val messageId: String,
    val sender: ChatParticipant,
    val isPictureShowable: Boolean,
    val message: Message,
    val messageOwner: MessageOwner
) {
    val isCurrentUser = messageOwner is MessageOwner.Me
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageSheet) return false

        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }
}