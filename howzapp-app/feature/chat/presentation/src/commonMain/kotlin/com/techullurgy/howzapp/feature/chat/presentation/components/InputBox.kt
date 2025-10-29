package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.attachment
import howzapp.core.presentation.generated.resources.audio
import howzapp.core.presentation.generated.resources.close
import howzapp.core.presentation.generated.resources.contact
import howzapp.core.presentation.generated.resources.emoji
import howzapp.core.presentation.generated.resources.image
import howzapp.core.presentation.generated.resources.location
import howzapp.core.presentation.generated.resources.plus
import howzapp.core.presentation.generated.resources.sticker
import howzapp.core.presentation.generated.resources.video
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun InputBox(
    modifier: Modifier = Modifier
) {
    var shouldAdditionBoxOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val state = rememberTextFieldState(initialText = "repeat".repeat(200))

        val isTextEmpty by remember {
            derivedStateOf { state.text.isEmpty() }
        }


        TextBox(
            state = state,
            isTextEmpty = isTextEmpty,
            modifier = Modifier.weight(1f),
            isAdditionBoxOpen = shouldAdditionBoxOpen,
            onAdditionIconClicked = { shouldAdditionBoxOpen = !shouldAdditionBoxOpen }
        )

        InputActionButton(
            actionState = if (isTextEmpty) InputActionState.Record else InputActionState.Send
        )
    }

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TextBox(
    state: TextFieldState,
    isTextEmpty: Boolean,
    isAdditionBoxOpen: Boolean,
    onAdditionIconClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        state = state,
        modifier = modifier
            .sizeIn(minHeight = 60.dp, maxHeight = 150.dp)
            .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(8.dp),
        decorator = { textField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val shouldShowPlaceholder = isTextEmpty
                val shouldShowAdditionBoxTrigger = isTextEmpty

                AnimatedVisibility(shouldShowAdditionBoxTrigger) {
                    AnimatedContent(
                        targetState = isAdditionBoxOpen,
                        label = "Addition Box Icon",
                        modifier = Modifier
                            .heightIn(max = 60.dp)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(Color.Red, CircleShape)
                            .clickable {
                                onAdditionIconClicked(!isAdditionBoxOpen)
                            }
                    ) { isOpen ->
                        if (isOpen) {
                            Icon(
                                painter = painterResource(Res.drawable.close),
                                contentDescription = null,
                                tint = LocalAppColors.current.content2
                            )
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.plus),
                                contentDescription = null,
                                tint = LocalAppColors.current.content2
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    androidx.compose.animation.AnimatedVisibility(visible = shouldShowPlaceholder) {
                        Text(
                            text = "Type a message here...",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    textField()
                }
            }
        }
    )
}

@Composable
private fun InputActionButton(
    actionState: InputActionState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .background(Color.Red)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = actionState,
            modifier = Modifier.matchParentSize()
        ) { state ->
            when (state) {
                InputActionState.Record -> {
                    Icon(
                        painter = painterResource(Res.drawable.audio),
                        contentDescription = "Record Audio",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                InputActionState.Send -> {
                    Icon(
                        painter = ColorPainter(Color.Green),
                        contentDescription = "Send",
                        tint = Color.Green,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

private sealed interface InputActionState {
    data object Send : InputActionState
    data object Record : InputActionState
}