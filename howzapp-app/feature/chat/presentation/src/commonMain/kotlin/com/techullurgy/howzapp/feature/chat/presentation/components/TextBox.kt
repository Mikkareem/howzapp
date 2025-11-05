package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.close
import howzapp.core.presentation.generated.resources.plus
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TextBox(
    state: TextFieldState,
    canOpenMoreInputSheet: Boolean,
    isAdditionBoxOpen: Boolean,
    onAdditionIconClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val isTextEmpty by remember {
        derivedStateOf { state.text.isEmpty() }
    }

    BasicTextField(
        state = state,
        modifier = modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        decorator = { textField ->
            Row(
                verticalAlignment = Alignment.Top
            ) {
                AnimatedVisibility(canOpenMoreInputSheet) {
                    Crossfade(
                        targetState = isAdditionBoxOpen,
                        label = "Addition Box Icon",
                        modifier = Modifier
                            .heightIn(max = 36.dp)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(Color.Red, CircleShape)
                            .clickable {
                                onAdditionIconClicked(!isAdditionBoxOpen)
                            }
                    ) { isOpen ->
                        if (isOpen) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.close),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.plus),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

                InnerTextFieldWithPlaceholder(
                    shouldShowPlaceholder = isTextEmpty,
                    textField = textField,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                )
            }
        }
    )
}

@Composable
private fun InnerTextFieldWithPlaceholder(
    shouldShowPlaceholder: Boolean,
    modifier: Modifier = Modifier,
    textField: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = shouldShowPlaceholder) {
            Text(
                text = "Type a message here...",
                style = MaterialTheme.typography.bodySmall
            )
        }
        textField()
    }
}