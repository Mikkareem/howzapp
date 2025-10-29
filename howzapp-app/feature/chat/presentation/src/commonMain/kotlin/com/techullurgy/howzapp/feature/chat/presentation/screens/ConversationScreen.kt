package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBox
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationUiAction
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationUiState
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

data class ConversationKey(
    val conversationId: String
)

@Composable
fun ConversationScreen(
    key: ConversationKey
) {
    val viewModel = koinViewModel<ConversationViewModel> {
        parametersOf(key)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onAction(ConversationUiAction.SendReadReceiptsIfAny)
        }
    }


    val state by viewModel.state.collectAsState()

    ConversationScreen(
        state = state
    )
}

@Composable
private fun ConversationScreen(
    state: ConversationUiState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ConversationScreenTopBar(state) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .padding(8.dp)
            )
            InputBox()
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(state.messageSheets) { index, sheet ->
                MessageBox(sheet)

                if(index+1 <= state.messageSheets.lastIndex) {
                    if(state.messageSheets[index+1].isCurrentUser != sheet.isCurrentUser) {
                        Spacer(Modifier.height(16.dp))
                    } else {
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationScreenTopBar(state: ConversationUiState) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.container1)
            .statusBarsPadding()
            .displayCutoutPadding()
            .padding(8.dp, 16.dp)
    ) {
        InfoBox(state)
    }
}

@Preview
@Composable
private fun ConversationScreenPreview(
    @PreviewParameter(ConversationUiStatePreviewParameterProvider::class) state: ConversationUiState
) {
    HowzAppTheme {
        ConversationScreen(state)
    }
}

private class ConversationUiStatePreviewParameterProvider :
    PreviewParameterProvider<ConversationUiState> {
    private val sampleMessageSheet = MessageSheet(
        messageId = "m1",
        sender = ChatParticipant("", ""),
        isPictureShowable = false,
        message = OriginalMessage.TextMessage(""),
        messageOwner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.SenderStatus.SENT),
        timestamp = Clock.System.now()
    )


    override val values: Sequence<ConversationUiState>
        get() = sequenceOf(
            ConversationUiState(
                title = "Irsath Kareem",
                subtitle = "Online",
                messageSheets = listOf(
                    sampleMessageSheet.copy(
                        message = OriginalMessage.ImageMessage(""),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.ImageMessage(""),
                            status = UploadStatus.Progress(28.0)
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Progress(28.0)
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Failed()
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),

                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Success("")
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(3.minutes)
                    ),
                )
            )
        )
}