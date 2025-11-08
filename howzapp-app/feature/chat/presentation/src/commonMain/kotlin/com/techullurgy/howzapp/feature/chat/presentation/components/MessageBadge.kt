package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.extended
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
internal fun MessageBadge(
    badge: MessageUi.Badge,
    modifier: Modifier = Modifier,
    isDividerPresent: Boolean = true
) {
    val badgeColor = MaterialTheme.colorScheme.extended.cakeBlue

    Box(
        modifier = modifier
            .drawBehind {
                if (isDividerPresent) {
                    val start = Offset(0f, center.y)
                    val end = Offset(size.width, center.y)

                    drawLine(
                        start = start,
                        end = end,
                        color = badgeColor,
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .drawBehind {
                    val path = Path().apply {
                        val dist = size.width * .1f

                        moveTo(0f, center.y)
                        lineTo(dist, 0f)
                        lineTo(size.width - dist, 0f)
                        lineTo(size.width, center.y)
                        lineTo(size.width - dist, size.height)
                        lineTo(dist, size.height)
                        close()
                    }

                    drawPath(
                        color = badgeColor,
                        path = path
                    )
                }
                .padding(horizontal = 24.dp, vertical = 2.dp)
        ) {
            Text(badge.badge, color = LocalContentColor.current)
        }
    }
}

@Composable
@Preview
private fun MessageBadgePreview(
    @PreviewParameter(BadgePreviewProvider::class) state: PreviewState
) {
    HowzAppTheme(state.isDarkMode) {
        Box(modifier = Modifier.padding(16.dp)) {
            MessageBadge(
                badge = MessageUi.Badge(state.badge)
            )
        }
    }
}

private data class PreviewState(
    val isDarkMode: Boolean,
    val badge: String
)

private class BadgePreviewProvider : PreviewParameterProvider<PreviewState> {
    override val values: Sequence<PreviewState>
        get() = sequence {
            sequenceOf(
                "24 unread messages", "18 OCT 2025", "Today", "Yesterday", "24 JUN"
            ).forEach { badge ->
                sequenceOf(true, false).forEach { dark ->
                    yield(PreviewState(dark, badge))
                }
            }
        }
}