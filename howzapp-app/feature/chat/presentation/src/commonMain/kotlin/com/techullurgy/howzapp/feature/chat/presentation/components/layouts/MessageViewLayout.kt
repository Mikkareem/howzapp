package com.techullurgy.howzapp.feature.chat.presentation.components.layouts

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.extended
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.presentation.components.AnchoredMessage
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageView
import kotlin.time.Instant

@Composable
internal fun MessageViewLayout(
    view: MessageAnchorState,
    message: Message,
    owner: MessageOwner,
    timestamp: Instant,
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit,
    onLocationMessageClick: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor =
        if (owner is MessageOwner.Me) MaterialTheme.colorScheme.extended.accentOrange else MaterialTheme.colorScheme.extended.tileNeutral

    val contentColor =
        if (owner is MessageOwner.Me) MaterialTheme.colorScheme.extended.onAccentOrange else MaterialTheme.colorScheme.extended.onTileNeutral

    val contents = mutableListOf<@Composable () -> Unit>().apply {
        val messageView = @Composable {
            MessageView(
                message,
                owner,
                timestamp,
                backgroundColor,
                onImageMessageClick,
                onVideoMessageClick,
                onLocationMessageClick
            )
        }

        if (view is MessageAnchorState.Anchored) {
            add(view.content)
            add {
                AnchoredMessage(
                    arrowDirection = view.direction,
                    color = backgroundColor,
                    content = messageView
                )
            }
        } else {
            add(messageView)
        }
    }.toList()

    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Layout(
            contents = contents,
            modifier = modifier
        ) { measurables, constraints ->
            val anchoredPlaceableSize = 36.dp.roundToPx()

            val anchoredPlaceable = view
                .takeIf { it is MessageAnchorState.Anchored }
                ?.let {
                    measurables.first().first().measure(
                        Constraints.fixed(anchoredPlaceableSize, anchoredPlaceableSize)
                    )
                }

            val anchorSize = 50

            val messagePlaceable = view
                .takeIf { it is MessageAnchorState.NonAnchored }
                ?.let {
                    measurables.first().first().measure(
                        constraints.copy(
                            maxWidth = (constraints.maxWidth - anchorSize - anchoredPlaceableSize)
                                .coerceAtMost(250.dp.roundToPx())
                        )
                    )
                }
                ?: run {
                    measurables.last().first().measure(
                        constraints.copy(
                            maxWidth = (constraints.maxWidth - anchorSize - anchoredPlaceableSize)
                                .coerceAtMost(250.dp.roundToPx())
                        )
                    )
                }

            val totalWidth = anchoredPlaceableSize + anchorSize + messagePlaceable.width
            val totalHeight = maxOf(anchoredPlaceableSize, messagePlaceable.height)

            layout(totalWidth, totalHeight) {
                when (view.direction) {
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

}

internal sealed interface MessageAnchorState {
    val direction: LayoutDirection

    data class Anchored(
        override val direction: LayoutDirection,
        val content: @Composable () -> Unit
    ) : MessageAnchorState

    data class NonAnchored(
        override val direction: LayoutDirection
    ) : MessageAnchorState
}