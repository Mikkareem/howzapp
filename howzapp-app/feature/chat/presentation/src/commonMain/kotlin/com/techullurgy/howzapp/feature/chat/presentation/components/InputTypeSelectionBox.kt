package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.attachment
import howzapp.core.presentation.generated.resources.audio
import howzapp.core.presentation.generated.resources.contact
import howzapp.core.presentation.generated.resources.emoji
import howzapp.core.presentation.generated.resources.image
import howzapp.core.presentation.generated.resources.location
import howzapp.core.presentation.generated.resources.sticker
import howzapp.core.presentation.generated.resources.video
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun InputTypeSelectionBox(
    inputTypes: List<InputType>,
    onInputTypeSelected: (InputTypeSelection) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
    ) {
        items(inputTypes) { type ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                val icon: @Composable () -> Unit = {
                    when (type) {
                        InputType.Attachment -> {
                            DocumentPicker(
                                onDocumentSelected = {
                                    onInputTypeSelected(
                                        InputTypeSelection.OnAttachmentSelected(
                                            it,
                                            ""
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.attachment),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Audio -> {
                            AudioPicker(
                                onAudioSelected = {
                                    onInputTypeSelected(InputTypeSelection.OnAudioSelected(it))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.audio),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Contact -> {
                            ContactPicker(
                                onContactSelected = { name, phoneNumber ->
                                    onInputTypeSelected(
                                        InputTypeSelection.OnContactSelected(
                                            name,
                                            phoneNumber
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.contact),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Emoji -> {
                            EmojiPicker(
                                onEmojiSelected = {
                                    onInputTypeSelected(InputTypeSelection.OnEmojiSelected(it))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.emoji),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Image -> {
                            ImagePicker(
                                onImageSelected = {
                                    onInputTypeSelected(InputTypeSelection.OnImageSelected(it))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.image),
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                            }
                        }

                        InputType.Location -> {
                            LocationPicker(
                                onLocationSelected = { latitude, longitude ->
                                    onInputTypeSelected(
                                        InputTypeSelection.OnLocationSelected(
                                            latitude,
                                            longitude
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.location),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Sticker -> {
                            StickerPicker(
                                onStickerSelected = {
                                    onInputTypeSelected(InputTypeSelection.OnStickerSelected(it))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.sticker),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        InputType.Video -> {
                            VideoPicker(
                                onVideoSelected = {
                                    onInputTypeSelected(InputTypeSelection.OnVideoSelected(it))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.video),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
                val title = when (type) {
                    InputType.Attachment -> "Docs"
                    InputType.Audio -> "Audio"
                    InputType.Contact -> "Contact"
                    InputType.Emoji -> "Emoji"
                    InputType.Image -> "Image"
                    InputType.Location -> "Location"
                    InputType.Sticker -> "Sticker"
                    InputType.Video -> "Video"
                }

                Box(
                    Modifier.size(48.dp)
                        .background(Color.Magenta, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

internal sealed interface InputType {
    data object Emoji : InputType
    data object Sticker : InputType
    data object Image : InputType
    data object Video : InputType
    data object Audio : InputType
    data object Attachment : InputType
    data object Location : InputType
    data object Contact : InputType
}

internal sealed interface InputTypeSelection {
    data class OnImageSelected(val imageUrl: String) : InputTypeSelection
    data class OnVideoSelected(val videoUrl: String) : InputTypeSelection
    data class OnAudioSelected(val audioUrl: String) : InputTypeSelection
    data class OnEmojiSelected(val emoji: String) : InputTypeSelection
    data class OnStickerSelected(val stickerUrl: String) : InputTypeSelection
    data class OnAttachmentSelected(val documentUrl: String, val documentName: String) :
        InputTypeSelection

    data class OnLocationSelected(val latitude: Double, val longitude: Double) : InputTypeSelection
    data class OnContactSelected(val name: String, val phoneNumber: String) : InputTypeSelection
}

@Preview
@Composable
private fun InputSelectionBoxPreview() {
    HowzAppTheme {
        InputTypeSelectionBox(
            inputTypes = listOf(
                InputType.Emoji,
                InputType.Image,
                InputType.Video,
                InputType.Audio,
                InputType.Sticker,
                InputType.Location,
                InputType.Contact,
                InputType.Attachment
            ),
            onInputTypeSelected = {}
        )
    }
}