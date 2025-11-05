package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.system.media.AudioRecordTrack
import com.techullurgy.howzapp.feature.chat.presentation.components.layouts.InputBoxLayout
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.InputMessagePreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
internal fun InputBox(
    inputState: ConversationInputUiState,
    modifier: Modifier = Modifier,
    onRecordStarted: () -> Unit,
    onRecordStopped: () -> Unit,
    onRecordCancelled: () -> Unit,
    onMessageSend: () -> Unit,
    onImageSelected: (String) -> Unit,
    onAudioSelected: (String) -> Unit,
    onVideoSelected: (String) -> Unit,
    onDocumentSelected: (String, String) -> Unit,
    onPlayRecordedAudioPreview: () -> Unit,
    onPauseRecordedAudioPreview: () -> Unit,
    onResumeRecordedAudioPreview: () -> Unit,
    onStopRecordedAudioPreview: () -> Unit,
    onPlayAudioPreview: () -> Unit,
    onPauseAudioPreview: () -> Unit,
    onResumeAudioPreview: () -> Unit,
    onStopAudioPreview: () -> Unit,
    onPlayVideoPreview: () -> Unit,
    onPauseVideoPreview: () -> Unit,
    onResumeVideoPreview: () -> Unit,
    onStopVideoPreview: () -> Unit,
) {
    var shouldAdditionBoxOpen by remember { mutableStateOf(false) }

    InputBoxLayout(
        main = {
            AnimatedContent(
                inputState.audioRecordTrack?.isRecording ?: false
            ) {
                if (it) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${inputState.audioRecordTrack!!.duration}")
                    }
                } else {
                    TextBox(
                        state = inputState.textState,
                        canOpenMoreInputSheet = inputState.canOpenMoreInputSheet,
                        isAdditionBoxOpen = shouldAdditionBoxOpen,
                        onAdditionIconClicked = { shouldAdditionBoxOpen = !shouldAdditionBoxOpen },
                    )
                }
            }
        },
        button = {
            InputActionButton(
                canRecordAudio = inputState.canRecordAudio,
                isRecording = inputState.audioRecordTrack?.isRecording ?: false,
                onRecordStarted = onRecordStarted,
                onRecordEnded = onRecordStopped,
                onRecordCancelled = onRecordCancelled,
                onMessageSend = onMessageSend
            )
        },
        preview = {
            PreviewBox(
                preview = inputState.inputMessagePreview,
                onPlayRecordedAudioPreview = onPlayRecordedAudioPreview,
                onPauseRecordedAudioPreview = onPauseRecordedAudioPreview,
                onResumeRecordedAudioPreview = onResumeRecordedAudioPreview,
                onStopRecordedAudioPreview = onStopRecordedAudioPreview,
                onPlayAudioPreview = onPlayAudioPreview,
                onPauseAudioPreview = onPauseAudioPreview,
                onResumeAudioPreview = onResumeAudioPreview,
                onStopAudioPreview = onStopAudioPreview,
                onPlayVideoPreview = onPlayVideoPreview,
                onPauseVideoPreview = onPauseVideoPreview,
                onResumeVideoPreview = onResumeVideoPreview,
                onStopVideoPreview = onStopVideoPreview,
            )
        },
        overlay = {
            InputTypeSelectionOverlay(
                canOpenOverlay = inputState.canOpenMoreInputSheet && shouldAdditionBoxOpen,
                onImageSelected = onImageSelected,
                onAudioSelected = onAudioSelected,
                onVideoSelected = onVideoSelected,
                onDocumentSelected = onDocumentSelected
            )
        },
        modifier = modifier.background(MaterialTheme.colorScheme.inversePrimary).padding(8.dp)
    )
}

@Composable
@Preview
private fun InputBoxPreview(
    @PreviewParameter(InputBoxStateProvider::class) state: InputBoxState
) {
    HowzAppTheme(state.isDarkMode) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            InputBox(
                inputState = state.state,
                onRecordStarted = {},
                onRecordStopped = {},
                onRecordCancelled = {},
                onMessageSend = {},
                onImageSelected = {},
                onAudioSelected = {},
                onVideoSelected = {},
                onDocumentSelected = {_, _ ->},
                onPlayRecordedAudioPreview = {},
                onPauseRecordedAudioPreview = {},
                onResumeRecordedAudioPreview = {},
                onStopRecordedAudioPreview = {},
                onPlayAudioPreview = {},
                onPauseAudioPreview = {},
                onResumeAudioPreview = {},
                onStopAudioPreview = {},
                onPlayVideoPreview = {},
                onPauseVideoPreview = {},
                onResumeVideoPreview = {},
                onStopVideoPreview = {}
            )
        }
    }
}

private data class InputBoxState(
    val isDarkMode: Boolean,
    val state: ConversationInputUiState
)

private class InputBoxStateProvider: PreviewParameterProvider<InputBoxState> {
    override val values: Sequence<InputBoxState> = sequence {
        darkModes.forEach { isDarkMode ->
            audioRecordTracks.forEach { audioRecordTrack ->
                inputMessagePreviews.forEach { inputPreview ->
                    texts.forEach { text ->
                        if(audioRecordTrack?.isRecording == true) {
                            yield(
                                InputBoxState(
                                    isDarkMode = isDarkMode,
                                    state = ConversationInputUiState(
                                        audioRecordTrack = audioRecordTrack,
                                    )
                                )
                            )
                        } else {
                            yield(
                                InputBoxState(
                                    isDarkMode = isDarkMode,
                                    state = ConversationInputUiState(
                                        audioRecordTrack = audioRecordTrack,
                                        inputMessagePreview = inputPreview,
                                        textState = TextFieldState(text)
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private val darkModes = sequenceOf(false)

private val audioRecordTracks = sequenceOf<AudioRecordTrack?>(
//    null,
    AudioRecordTrack(id = "", isRecording = true, recordingPath = "", duration = 3428349),
//    AudioRecordTrack(id = "", isRecording = false, recordingPath = "", duration = 3428349),
)

private val inputMessagePreviews = sequenceOf<InputMessagePreview?>(
    null,
//    InputMessagePreview.RecordedAudioPreview(recordedPath = "", duration = 3428349, isPlaying = false, durationPlayed = 0),
//    InputMessagePreview.RecordedAudioPreview(recordedPath = "", duration = 3428349, isPlaying = false, durationPlayed = 23891),
//    InputMessagePreview.RecordedAudioPreview(recordedPath = "", duration = 3428349, isPlaying = true, durationPlayed = 238917)
)

private val texts = sequenceOf(
    "",
    "repeat ".repeat(10), "repeat".repeat(100), "repeat".repeat(10), "repeat ".repeat(100)
)