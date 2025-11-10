package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.core.system.media.AudioRecordTrack
import com.techullurgy.howzapp.core.system.media.MediaHandler
import com.techullurgy.howzapp.core.system.media.PlaybackState
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.usecases.NewPendingMessageUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.RecordingAudioNotifierUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.TypingNotifierUsecase
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.ConversationKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
internal class ConversationInputViewModel(
    private val key: ConversationKey,
    private val mediaHandler: MediaHandler,
    private val newPendingMessage: NewPendingMessageUsecase,
    private val typingNotifier: TypingNotifierUsecase,
    private val recordingAudioNotifier: RecordingAudioNotifierUsecase
) : ViewModel() {

    private val _inputState = MutableStateFlow(ConversationInputUiState())
    val inputState = _inputState
        .onStart {
            observeVideoPlayer()
            observeAudioPlayer()
            observeRecordedAudioPlayer()
            observeAudioRecorder()
            observeIsTypingMessage()
            observeIsRecordingAudio()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _inputState.value
        )

    fun onAction(action: ConversationInputUiAction) {
        when (action) {
            ConversationInputUiAction.OnAudioRecordStarted -> {
                startRecording()
            }

            ConversationInputUiAction.OnAudioRecordStopped -> {
                stopRecording()
            }

            ConversationInputUiAction.OnAudioRecordCancelled -> {
                cancelRecording()
            }

            ConversationInputUiAction.OnMessageSend -> {
                sendMessage()
            }

            is ConversationInputUiAction.OnAudioSelected -> {
                setAudioPreview(action.audioUrl)
            }

            is ConversationInputUiAction.OnDocumentSelected -> {
                setDocumentPreview(action.documentName, action.documentUrl)
            }

            is ConversationInputUiAction.OnImageSelected -> {
                setImagePreview(action.imageUrl)
            }

            is ConversationInputUiAction.OnVideoSelected -> {
                setVideoPreview(action.videoUrl)
            }

            ConversationInputUiAction.OnPauseRecordedAudio -> {
                pauseRecordedAudioPreview()
            }

            ConversationInputUiAction.OnPlayRecordedAudio -> {
                playRecordedAudioPreview()
            }

            ConversationInputUiAction.OnResumeRecordedAudio -> {
                resumeRecordedAudioPreview()
            }

            ConversationInputUiAction.OnStopRecordedAudio -> {
                stopRecordedAudioPreview()
            }

            ConversationInputUiAction.OnPauseAudioPreview -> {
                pauseAudioPreview()
            }

            ConversationInputUiAction.OnPauseVideoPreview -> {
                pauseVideoPreview()
            }

            ConversationInputUiAction.OnPlayAudioPreview -> {
                playAudioPreview()
            }

            ConversationInputUiAction.OnPlayVideoPreview -> {
                playVideoPreview()
            }

            ConversationInputUiAction.OnResumeAudioPreview -> {
                resumeAudioPreview()
            }

            ConversationInputUiAction.OnResumeVideoPreview -> {
                resumeVideoPreview()
            }

            ConversationInputUiAction.OnStopAudioPreview -> {
                stopAudioPreview()
            }

            ConversationInputUiAction.OnStopVideoPreview -> {
                stopVideoPreview()
            }
        }
    }

    private fun observeAudioRecorder() {
        mediaHandler.activeAudioRecordTrack
            .onEach {
                if (it == null) {
                    _inputState.update { state ->
                        state.copy(
                            audioRecordTrack = null
                        )
                    }
                    return@onEach
                }

                if (it.id == key.conversationId) {
                    if (it.isRecording) {
                        _inputState.update { state ->
                            state.copy(
                                audioRecordTrack = it
                            )
                        }
                    } else {
                        if (it.duration < 3.seconds.inWholeMilliseconds) {
                            mediaHandler.cancelAudioRecording()
                        } else {
                            recorded(it.recordingPath, it.duration)
                            mediaHandler.resetAudioRecording()
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeIsTypingMessage() {
        snapshotFlow { inputState.value.textState.text.toString() }
            .drop(1)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .conflate()
            .onEach {
                typingNotifier.invoke(key.conversationId)
                delay(2000)
            }
            .launchIn(viewModelScope)
    }

    private fun observeIsRecordingAudio() {
        inputState
            .map { it.audioRecordTrack?.takeIf { d -> d.id == key.conversationId } }
            .filterNotNull()
            .conflate()
            .onEach {
                if (it.isRecording) {
                    recordingAudioNotifier.invoke(key.conversationId)
                    delay(2000)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeAudioPlayer() {
        mediaHandler.activeAudioTrack
            .transform { track ->
                if (track == null || track.id != "preview_recorded_audio_{${key.conversationId}}") {
                    emit(null)
                } else {
                    emit(track)
                }
            }
            .onEach { track ->

                if (track == null) {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.SelectedAudioPreview) {
                                it.inputMessagePreview.copy(isPlaying = false, durationPlayed = 0)
                            } else it.inputMessagePreview
                        )
                    }
                } else {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.SelectedAudioPreview) {
                                it.inputMessagePreview.copy(
                                    isPlaying = track.isPlaying,
                                    durationPlayed = track.durationPlayed,
                                    duration = track.totalDuration
                                )
                            } else it.inputMessagePreview
                        )
                    }
                }

            }
            .launchIn(viewModelScope)
    }

    private fun observeRecordedAudioPlayer() {
        mediaHandler.activeAudioTrack
            .transform { track ->
                if (track == null || track.id != "preview_recorded_audio_{${key.conversationId}}") {
                    emit(null)
                } else {
                    emit(track)
                }
            }
            .onEach { track ->

                if (track == null) {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.RecordedAudioPreview) {
                                it.inputMessagePreview.copy(isPlaying = false, durationPlayed = 0)
                            } else it.inputMessagePreview
                        )
                    }
                } else {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.RecordedAudioPreview) {
                                it.inputMessagePreview.copy(
                                    isPlaying = track.isPlaying,
                                    durationPlayed = track.durationPlayed,
                                    duration = track.totalDuration
                                )
                            } else it.inputMessagePreview
                        )
                    }
                }

            }
            .launchIn(viewModelScope)
    }

    private fun observeVideoPlayer() {
        mediaHandler.activeVideoTrack
            .transform { track ->
                if (track == null || track.id != "preview_video_{${key.conversationId}}") {
                    emit(null)
                } else {
                    emit(track)
                }
            }
            .onEach { track ->
                if (track == null) {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.SelectedVideoPreview) {
                                it.inputMessagePreview.copy(
                                    isPlaying = false,
                                    durationPlayed = 0,
                                    isLoading = false
                                )
                            } else it.inputMessagePreview
                        )
                    }
                } else {
                    _inputState.update {
                        it.copy(
                            inputMessagePreview = if (it.inputMessagePreview is InputMessagePreview.SelectedVideoPreview) {
                                it.inputMessagePreview.copy(durationPlayed = track.durationPlayed)
                                when (track.state) {
                                    PlaybackState.Buffering -> it.inputMessagePreview.copy(
                                        isLoading = true,
                                        isPlaying = false
                                    )

                                    PlaybackState.Paused -> it.inputMessagePreview.copy(
                                        isPlaying = false,
                                        isLoading = false
                                    )

                                    PlaybackState.Playing -> it.inputMessagePreview.copy(
                                        isPlaying = true,
                                        isLoading = false
                                    )

                                    PlaybackState.Stopped -> it.inputMessagePreview.copy(
                                        isPlaying = false,
                                        isLoading = false
                                    )
                                }
                            } else it.inputMessagePreview
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setAudioPreview(audioUrl: String) {
        _inputState.update {
            it.copy(
                inputMessagePreview = InputMessagePreview.SelectedAudioPreview(audioUrl)
            )
        }
    }

    private fun setVideoPreview(videoUrl: String) {
        _inputState.update {
            it.copy(
                inputMessagePreview = InputMessagePreview.SelectedVideoPreview(videoUrl)
            )
        }
    }

    private fun setImagePreview(imageUrl: String) {
        _inputState.update {
            it.copy(
                inputMessagePreview = InputMessagePreview.SelectedImagePreview(imageUrl)
            )
        }
    }

    private fun setDocumentPreview(documentName: String, documentUrl: String) {
        _inputState.update {
            it.copy(
                inputMessagePreview = InputMessagePreview.SelectedDocumentPreview(
                    documentName,
                    documentUrl
                )
            )
        }
    }

    private fun sendMessage() {
        when (val currentInputMessage = _inputState.value.inputMessagePreview) {
            is InputMessagePreview.RecordedAudioPreview -> newAudioMessage(currentInputMessage.recordedPath)
            is InputMessagePreview.SelectedAudioPreview -> newAudioMessage(currentInputMessage.audioUrl)
            is InputMessagePreview.SelectedDocumentPreview -> newDocumentMessage(
                currentInputMessage.documentName,
                currentInputMessage.documentUrl
            )

            is InputMessagePreview.SelectedImagePreview -> newImageMessage(currentInputMessage.imageUrl)
            is InputMessagePreview.SelectedVideoPreview -> newVideoMessage(currentInputMessage.videoUrl)
            null -> newTextMessage(_inputState.value.textState.text.toString())
        }
    }

    private fun newAudioMessage(audioUrl: String) {
        val audioMessage = OriginalMessage.AudioMessage(audioUrl)
        newPendingMessage(audioMessage)
    }

    private fun newVideoMessage(videoUrl: String) {
        val videoMessage = OriginalMessage.VideoMessage(videoUrl)
        newPendingMessage(videoMessage)
    }

    private fun newImageMessage(imageUrl: String) {
        val imageMessage = OriginalMessage.ImageMessage(imageUrl)
        newPendingMessage(imageMessage)
    }

    private fun newDocumentMessage(documentName: String, documentUrl: String) {
        val documentMessage = OriginalMessage.DocumentMessage(documentName, documentUrl)
        newPendingMessage(documentMessage)
    }

    private fun newTextMessage(text: String) {
        val textMessage = OriginalMessage.TextMessage(text)
        newPendingMessage(textMessage)
    }

    private fun newPendingMessage(message: OriginalMessage) {
        viewModelScope.launch {
            val chatId = key.conversationId
            newPendingMessage(chatId, message)
        }
    }

    private fun playVideoPreview() {
        val videoPreview =
            (inputState.value.inputMessagePreview as? InputMessagePreview.SelectedVideoPreview)
                ?: return
        mediaHandler.playVideo(
            "preview_video_{${key.conversationId}}",
            videoPreview.videoUrl,
            onComplete = {})
    }

    private fun pauseVideoPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedVideoPreview) return
        mediaHandler.pauseVideo()
    }

    private fun resumeVideoPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedVideoPreview) return
        mediaHandler.resumeVideo()
    }

    private fun stopVideoPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedVideoPreview) return
        mediaHandler.stopVideo()
    }

    private fun playAudioPreview() {
        val audioPreview =
            (inputState.value.inputMessagePreview as? InputMessagePreview.SelectedAudioPreview)
                ?: return
        mediaHandler.playAudio(
            "preview_audio_{${key.conversationId}}",
            audioPreview.audioUrl,
            onComplete = {})
    }

    private fun pauseAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedAudioPreview) return
        mediaHandler.pauseAudio()
    }

    private fun resumeAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedAudioPreview) return
        mediaHandler.resumeAudio()
    }

    private fun stopAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.SelectedAudioPreview) return
        mediaHandler.stopAudio()
    }

    private fun playRecordedAudioPreview() {
        val recordedAudioPreview =
            (inputState.value.inputMessagePreview as? InputMessagePreview.RecordedAudioPreview)
                ?: return
        mediaHandler.playAudio(
            id = "preview_recorded_audio_{${key.conversationId}}",
            filePath = recordedAudioPreview.recordedPath,
            onComplete = {}
        )
    }

    private fun pauseRecordedAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.RecordedAudioPreview) return
        mediaHandler.pauseAudio()
    }

    private fun resumeRecordedAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.RecordedAudioPreview) return
        mediaHandler.resumeAudio()
    }

    private fun stopRecordedAudioPreview() {
        if (inputState.value.inputMessagePreview !is InputMessagePreview.RecordedAudioPreview) return
        mediaHandler.stopAudio()
    }

    private fun startRecording() {
        val fileName = "${key.conversationId}-${System.currentTimeMillis()}"
        mediaHandler.startAudioRecording(key.conversationId, fileName)
    }

    private fun stopRecording() {
        mediaHandler.stopAudioRecording()
    }

    private fun cancelRecording() {
        mediaHandler.cancelAudioRecording()
    }

    private fun recorded(fileUrl: String, duration: Long) {
        _inputState.update {
            it.copy(
                inputMessagePreview = InputMessagePreview.RecordedAudioPreview(
                    recordedPath = fileUrl,
                    duration = duration.toInt()
                )
            )
        }
    }
}

@Stable
internal sealed interface InputMessagePreview {
    @Stable
    data class RecordedAudioPreview(
        val recordedPath: String,
        val duration: Int,
        val isPlaying: Boolean = false,
        val durationPlayed: Int = 0
    ) : InputMessagePreview

    @Stable
    data class SelectedImagePreview(val imageUrl: String) : InputMessagePreview

    @Stable
    data class SelectedVideoPreview(
        val videoUrl: String,
        val duration: Long = 0,
        val isLoading: Boolean = false,
        val isPlaying: Boolean = false,
        val durationPlayed: Long = 0
    ) : InputMessagePreview

    @Stable
    data class SelectedAudioPreview(
        val audioUrl: String,
        val duration: Int = 0,
        val isPlaying: Boolean = false,
        val durationPlayed: Int = 0
    ) : InputMessagePreview

    @Stable
    data class SelectedDocumentPreview(val documentName: String, val documentUrl: String) :
        InputMessagePreview
}

@Stable
internal data class ConversationInputUiState(
    val audioRecordTrack: AudioRecordTrack? = null,
    val inputMessagePreview: InputMessagePreview? = null,
    val textState: TextFieldState = TextFieldState()
) {
    private val isSendable: Boolean = textState.text.isNotBlank() || inputMessagePreview != null
    val canRecordAudio: Boolean = !isSendable
    val canOpenMoreInputSheet: Boolean = inputMessagePreview == null
}

internal sealed interface ConversationInputUiAction {
    data object OnAudioRecordStarted : ConversationInputUiAction
    data object OnAudioRecordStopped : ConversationInputUiAction
    data object OnAudioRecordCancelled : ConversationInputUiAction
    data object OnMessageSend : ConversationInputUiAction
    data class OnImageSelected(val imageUrl: String) : ConversationInputUiAction
    data class OnAudioSelected(val audioUrl: String) : ConversationInputUiAction
    data class OnVideoSelected(val videoUrl: String) : ConversationInputUiAction
    data class OnDocumentSelected(val documentName: String, val documentUrl: String) :
        ConversationInputUiAction

    data object OnPlayRecordedAudio : ConversationInputUiAction
    data object OnPauseRecordedAudio : ConversationInputUiAction
    data object OnResumeRecordedAudio : ConversationInputUiAction
    data object OnStopRecordedAudio : ConversationInputUiAction

    data object OnPlayAudioPreview : ConversationInputUiAction
    data object OnPauseAudioPreview : ConversationInputUiAction
    data object OnResumeAudioPreview : ConversationInputUiAction
    data object OnStopAudioPreview : ConversationInputUiAction

    data object OnPlayVideoPreview : ConversationInputUiAction
    data object OnPauseVideoPreview : ConversationInputUiAction
    data object OnResumeVideoPreview : ConversationInputUiAction
    data object OnStopVideoPreview : ConversationInputUiAction
}