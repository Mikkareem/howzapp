package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.designsystem.resources.Icons
import com.techullurgy.howzapp.core.designsystem.theme.extended
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun RecordButton(
    isRecording: Boolean,
    onRecordStarted: () -> Unit,
    onRecordEnded: () -> Unit,
    onRecordCancelled: () -> Unit
) {
    val expandAnimation by animateFloatAsState(
        targetValue = if (isRecording) 1f else 0f
    )

    var slideRadius by remember { mutableFloatStateOf(0f) }

    val recordingColor = MaterialTheme.colorScheme.extended.accentGreen
    val cancellingColor = MaterialTheme.colorScheme.extended.accentRed

    val textMeasurer = rememberTextMeasurer()

    Icon(
        painter = Icons.recordingIcon,
        tint = Color.White,
        contentDescription = "Record Audio",
        modifier = Modifier
            .drawBehind {
                if (isRecording) {
                    drawCircle(
                        color = recordingColor,
                        radius = 200.dp.toPx() * expandAnimation
                    )

                    drawCircle(
                        color = cancellingColor,
                        radius = slideRadius
                    )

                    val slideToCancelLayoutResult = textMeasurer.measure("<<< Slide to cancel")
                    val holdToContinueRecordingLayoutResult = textMeasurer.measure(
                        "Hold to continue recording",
                        constraints = Constraints(maxWidth = 150.dp.roundToPx())
                    )

                    // Slide to cancel (TEXT)
                    drawText(
                        textLayoutResult = slideToCancelLayoutResult,
                        topLeft = Offset(
                            -slideToCancelLayoutResult.size.width.toFloat() - 40.dp.toPx(),
                            center.y - (slideToCancelLayoutResult.size.height / 2f)
                        )
                    )

                    // Hold to continue recording (TEXT)
                    drawText(
                        textLayoutResult = holdToContinueRecordingLayoutResult,
                        topLeft = Offset(
                            -100.dp.toPx(),
                            -100.dp.toPx()
                        )
                    )
                }
            }
            .pointerInput(Unit) {
                coroutineScope {
                    awaitEachGesture {
                        var holdStarted = false
                        var isManuallyCancelled = false

                        val down = awaitFirstDown()

                        val holdingJob = launch {
                            delay(1000)
                            holdStarted = true
                            onRecordStarted()
                        }

                        while (true) {
                            val result = drag(down.id) { drag ->
                                val dragChange = drag.position - down.position

                                if (dragChange != Offset.Zero) {
                                    val center = Offset(size.width * .5f, size.height * .5f)
                                    slideRadius = (drag.position - center).getDistance()
                                        .coerceAtMost(200.dp.toPx())
                                    if (slideRadius == 200.dp.toPx()) {
                                        isManuallyCancelled = true
                                    } else {
                                        isManuallyCancelled = false
                                    }
                                }
                            }
                            if (!result) break
                        }

                        holdingJob.cancel()
                        if (isManuallyCancelled) {
                            onRecordCancelled()
                        } else if (holdStarted) {
                            onRecordEnded()
                        } else {
                            onRecordCancelled()
                        }
                        slideRadius = 0f
                    }
                }
            }
    )
}

@Composable
@Preview
private fun RecordButtonPreview() {
    val recordState = remember { mutableStateOf(RecordState.Idle) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = recordState.value.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )

        Box(modifier = Modifier.align(Alignment.BottomStart).padding(64.dp)) {
            RecordButton(
                isRecording = recordState.value in listOf(
                    RecordState.Recording,
                    RecordState.Cancelling
                ),
                onRecordStarted = { recordState.value = RecordState.Recording },
                onRecordEnded = { recordState.value = RecordState.Completed },
                onRecordCancelled = { recordState.value = RecordState.Cancelled }
            )
        }
    }
}

private enum class RecordState {
    Idle,
    Recording,
    Cancelling,
    Cancelled,
    Completed
}