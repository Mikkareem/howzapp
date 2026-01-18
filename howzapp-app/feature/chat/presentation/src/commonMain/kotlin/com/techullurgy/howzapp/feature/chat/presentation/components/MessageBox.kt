package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.LayoutDirection
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.feature.chat.presentation.components.layouts.MessageViewLayout
import com.techullurgy.howzapp.feature.chat.presentation.components.layouts.MessageViewUi
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageUi

@Composable
internal fun MessageBox(
    content: MessageUi.Content,
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit,
    onLocationMessageClick: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val direction = if (content.content.isCurrentUser) LayoutDirection.Rtl else LayoutDirection.Ltr

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = when(direction) {
            LayoutDirection.Ltr -> Arrangement.Start
            LayoutDirection.Rtl -> Arrangement.End
        }
    ) {

        val view = if (content.content.isPictureShowable) {
            MessageViewUi.Anchored(
                direction = direction
            ) {
                Box(Modifier.clip(CircleShape).background(color = Color.Magenta)) {
                    AsyncImage(
                        model = content.content.messageOwner.owner.profilePictureUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else {
            MessageViewUi.NonAnchored(
                direction = direction
            )
        }

        MessageViewLayout(
            view = view,
            message = content.content.message,
            owner = content.content.messageOwner,
            timestamp = content.content.timestamp,
            onImageMessageClick = onImageMessageClick,
            onVideoMessageClick = onVideoMessageClick,
            onLocationMessageClick = onLocationMessageClick
        )
    }
}