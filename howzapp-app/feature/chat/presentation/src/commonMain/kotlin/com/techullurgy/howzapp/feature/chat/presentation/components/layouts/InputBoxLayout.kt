package com.techullurgy.howzapp.feature.chat.presentation.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun InputBoxLayout(
    modifier: Modifier = Modifier,
    main: @Composable BoxScope.() -> Unit,
    button: @Composable BoxScope.() -> Unit,
    overlay: @Composable BoxScope.() -> Unit = {},
    preview: @Composable BoxScope.() -> Unit = {}
) {

    val mainComposable: @Composable () -> Unit =
        remember(main) { { Box(propagateMinConstraints = true) { main() } } }
    val buttonComposable: @Composable () -> Unit = remember(button) { { Box { button() } } }
    val overlayComposable: @Composable () -> Unit = remember(overlay) { { Box { overlay() } } }
    val previewComposable: @Composable () -> Unit = remember(preview) { { Box { preview() } } }

    val components = listOf(mainComposable, buttonComposable, overlayComposable, previewComposable)

    Layout(
        modifier = modifier,
        contents = components
    ) { measurables, constraints ->

        val mainMeasurable = measurables[0].first()
        val buttonMeasurable = measurables[1].first()
        val overlayMeasurable = measurables[2].first()
        val previewMeasurable = measurables[3].first()

        val mainToButtonPadding = 8.dp.roundToPx()
        val mainToPreviewPadding = 16.dp.roundToPx()

        val buttonPlaceable = buttonMeasurable.measure(constraints)

        val mainPlaceable = constraints.copy(
            minWidth = constraints.maxWidth - buttonPlaceable.width - mainToButtonPadding,
            maxWidth = constraints.maxWidth - buttonPlaceable.width - mainToButtonPadding,
            minHeight = buttonPlaceable.height,
            maxHeight = 150.dp.roundToPx()
        ).run {
            mainMeasurable.measure(this)
        }

        val previewPlaceable = constraints.copy(
            maxWidth = mainPlaceable.width
        ).run { previewMeasurable.measure(this) }

        val overlayPlaceable = constraints.copy(
            maxWidth = (mainPlaceable.width * .75f).roundToInt()
        ).run { overlayMeasurable.measure(this) }

        val totalWidth = buttonPlaceable.width + mainPlaceable.width + mainToButtonPadding

        val totalHeight = mainPlaceable.height
            .plus(previewPlaceable.height)
            .plus(if(previewPlaceable.height > 0) mainToPreviewPadding else 0)

        layout(totalWidth, totalHeight) {
            previewPlaceable.place(0, 0)

            mainPlaceable.place(
                0,
                previewPlaceable.height.plus(if (previewPlaceable.height > 0) mainToPreviewPadding else 0)
            )

            buttonPlaceable.place(
                mainPlaceable.width + mainToButtonPadding,
                totalHeight - buttonPlaceable.height
            )

            overlayPlaceable.place(
                30.dp.roundToPx(),
                -overlayPlaceable.height + previewPlaceable.height,
                3f
            )
        }
    }
}

@Composable
@Preview
private fun InputBoxLayoutPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        var number by remember { mutableIntStateOf(0) }

        Text("$number", modifier = Modifier.align(Alignment.Center))

        InputBoxLayout(
            main = {
                Box(Modifier.matchParentSize().background(Color.Green))
            },
            button = {
                Box(Modifier.size(48.dp).background(Color.Magenta))
            },
            overlay = {
                Box(Modifier.fillMaxWidth().height(160.dp).background(Color.Blue)) {
                    Button(
                        onClick = { number += 1 }
                    ) {
                        Text("Increment")
                    }
                }
            },
            preview = {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color.Black))
            },
            modifier = Modifier.background(Color.Transparent)
        )
    }
}