package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationUiState

@Composable
internal fun InfoBox(state: ConversationUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        state.profilePicture?.let {
            AsyncImage(
                modifier = Modifier.size(60.dp),
                model = state.profilePicture,
                contentDescription = null,
            )
        } ?: run {
            Box(
                Modifier.size(60.dp)
                    .background(shape = CircleShape, color = LocalAppColors.current.container2)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                state.title,
                color = LocalAppColors.current.content1,
                style = MaterialTheme.typography.titleMedium
            )
            AnimatedVisibility(state.subtitle.isNotBlank()) {
                Text(
                    state.subtitle,
                    color = LocalAppColors.current.content1.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
