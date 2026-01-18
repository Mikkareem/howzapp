package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.plus
import howzapp.core.presentation.generated.resources.sent
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun PendingMessageView(
    message: PendingMessage,
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit
) {
    when (message) {
        is PendingMessage.NonUploadablePendingMessage -> NonUploadablePendingMessageView(message)
        is PendingMessage.UploadablePendingMessage -> UploadablePendingMessageView(
            message,
            onImageMessageClick,
            onVideoMessageClick
        )
    }
}

@Composable
private fun NonUploadablePendingMessageView(
    message: PendingMessage.NonUploadablePendingMessage
) {
    when (val originalMessage = message.originalMessage) {
        is OriginalMessage.TextMessage -> TextMessageView(originalMessage)
    }
}

@Composable
private fun UploadablePendingMessageView(
    message: PendingMessage.UploadablePendingMessage,
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        when (val originalMessage = message.originalMessage) {
            is OriginalMessage.AudioMessage -> {
                AudioMessageView(
                    message = originalMessage,
                    onPlay = {},
                    onPause = {}
                )
            }

            is OriginalMessage.DocumentMessage -> DocumentMessageView(originalMessage)
            is OriginalMessage.ImageMessage -> ImageMessageView(
                originalMessage,
                onImageMessageClick
            )

            is OriginalMessage.VideoMessage -> VideoMessageView(
                originalMessage,
                onVideoMessageClick
            )
        }

        Box(
            modifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        cornerRadius = CornerRadius(x = maxOf(size.width, size.height) / 2f)
                    )
                }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val status = message.status) {
                is UploadStatus.Failed -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Red
                            )
                        }
                        Text("Retry", color = Color.Red, fontSize = 14.sp)
                    }
                }

                is UploadStatus.Progress -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Red
                            )
                        }
                        Text("${status.progressPercentage}", color = Color.Green)
                    }
                }

                is UploadStatus.Started -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                            )
                        }
                        Text("Pending", fontSize = 14.sp)
                    }
                }

                is UploadStatus.Success -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.sent),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Green
                            )
                        }
                        Text("Uploaded", color = Color.Green, fontSize = 14.sp)
                    }
                }

                is UploadStatus.Triggered -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                            )
                        }
                        Text("Pending", fontSize = 14.sp)
                    }
                }

                else -> {}
            }
        }
    }
}
