package com.techullurgy.howzapp.feature.chat.presentation.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageView
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageViewAnchored

sealed interface MessageViewUi {
    val direction: LayoutDirection

    data class Anchored(
        override val direction: LayoutDirection,
        val content: @Composable () -> Unit
    ): MessageViewUi

    data class NonAnchored(
        override val direction: LayoutDirection
    ): MessageViewUi
}

@Composable
internal fun MessageViewLayout(
    view: MessageViewUi,
    message: Message,
    modifier: Modifier = Modifier
) {
    val contents = mutableListOf<@Composable () -> Unit>().apply {
        if(view is MessageViewUi.Anchored) {
            add(view.content)
            add {
                MessageViewAnchored(message, view.direction)
            }
        } else {
            add {
                MessageView(message)
            }
        }
    }.toList()

    Layout(
        contents = contents,
        modifier = modifier
    ) { measurables, constraints ->
        val anchoredPlaceableSize = 36.dp.roundToPx()

        val anchoredPlaceable = view
            .takeIf { it is MessageViewUi.Anchored }
            ?.let {
                measurables.first().first().measure(
                    Constraints.fixed(anchoredPlaceableSize, anchoredPlaceableSize)
                )
            }

        val anchorSize = 50

        val messagePlaceable = view
            .takeIf { it is MessageViewUi.NonAnchored }
            ?.let {
                measurables.first().first().measure(
                    constraints.copy(maxWidth = constraints.maxWidth - anchorSize - anchoredPlaceableSize)
                )
            }
            ?: run {
                measurables.last().first().measure(
                    constraints.copy(maxWidth = constraints.maxWidth - anchorSize - anchoredPlaceableSize)
                )
            }

        val totalWidth = anchoredPlaceableSize + anchorSize + messagePlaceable.width
        val totalHeight = maxOf(anchoredPlaceableSize, messagePlaceable.height)

        layout(totalWidth, totalHeight) {
            when(view.direction) {
                LayoutDirection.Ltr -> {
                    anchoredPlaceable?.place(0, 0)
                    val messagePlaceableX = anchoredPlaceableSize + anchorSize
                    messagePlaceable.place(messagePlaceableX, 0)
                }
                LayoutDirection.Rtl -> {
                    messagePlaceable.place(0, 0)
                    anchoredPlaceable?.let {
                        val anchorPlaceableX = messagePlaceable.width + anchorSize
                        it.place(anchorPlaceableX, 0)
                    }
                }
            }
        }
    }
}