package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import kotlin.math.roundToInt

@Composable
internal fun ImageMessageView(
    message: OriginalMessage.ImageMessage,
    onImageClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .layout(imageMessageMeasureResult)
            .aspectRatio(1f)
            .clickable {
                onImageClick(message.imageUrl)
            }
    ) {
        AsyncImage(
            model = message.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

private val imageMessageMeasureResult: MeasureScope.(Measurable, Constraints) -> MeasureResult =
    { measurable, constraints ->
        val imageWidth = minOf(constraints.maxWidth * .75f, 250.dp.toPx()).roundToInt()

        val newConstraints = constraints.copy(
            minWidth = imageWidth,
            maxWidth = imageWidth
        )

        val placeable = measurable.measure(newConstraints)

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
    }
}