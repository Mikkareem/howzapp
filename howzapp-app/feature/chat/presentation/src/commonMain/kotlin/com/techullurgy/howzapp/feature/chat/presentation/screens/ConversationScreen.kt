package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import com.techullurgy.howzapp.feature.chat.presentation.components.Addition
import com.techullurgy.howzapp.feature.chat.presentation.components.AdditionBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBox
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationUiState
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationViewModel
import howzapp.feature.chat.presentation.generated.resources.Res
import howzapp.feature.chat.presentation.generated.resources.attachment
import howzapp.feature.chat.presentation.generated.resources.audio
import howzapp.feature.chat.presentation.generated.resources.contact
import howzapp.feature.chat.presentation.generated.resources.emoji
import howzapp.feature.chat.presentation.generated.resources.image
import howzapp.feature.chat.presentation.generated.resources.location
import howzapp.feature.chat.presentation.generated.resources.sticker
import howzapp.feature.chat.presentation.generated.resources.video
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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
        bottomBar = { ConversationScreenBottomBar() }
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

@Composable
private fun ConversationScreenBottomBar() {

    var shouldAdditionBoxOpen by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(LocalAppColors.current.container1)
            .padding(8.dp, 16.dp)
    ) {
        InputBox(
            shouldAdditionBoxOpen = shouldAdditionBoxOpen,
            onAdditionIconClicked = {
                shouldAdditionBoxOpen = it
            }
        )

        AnimatedVisibility(shouldAdditionBoxOpen) {
            AdditionBox(
                additions = listOf(
                    Addition(Res.drawable.emoji, "Emoji", Color.Magenta),
                    Addition(Res.drawable.sticker, "Sticker", Color.Magenta),
                    Addition(Res.drawable.image, "Image", Color.Magenta),
                    Addition(Res.drawable.audio, "Record Audio", Color.Magenta),
                    Addition(Res.drawable.video, "Video", Color.Magenta),
                    Addition(Res.drawable.attachment, "Attachment", Color.Magenta),
                    Addition(Res.drawable.location, "Location", Color.Magenta),
                    Addition(Res.drawable.contact, "Contact", Color.Magenta),
                )
            )
        }
    }
}

@Preview
@Composable
private fun ConversationScreenPreview() {
    HowzAppTheme {
        ConversationScreen(
            ConversationUiState(
                title = "Irsath Kareem",
                subtitle = "Online",
                profilePicture = null
            )
        )
    }
}