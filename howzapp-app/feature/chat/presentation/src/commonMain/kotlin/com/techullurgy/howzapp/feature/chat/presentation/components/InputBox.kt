package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.close
import howzapp.core.presentation.generated.resources.plus
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun InputBox(
    shouldAdditionBoxOpen: Boolean,
    onAdditionIconClicked: (toggled: Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()) {

        Box(
            Modifier
                .size(48.dp)
                .background(
                    color = LocalAppColors.current.container2,
                    shape = CircleShape
                )
                .clickable(onClick = { onAdditionIconClicked(!shouldAdditionBoxOpen) }),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = shouldAdditionBoxOpen,
                label = "Addition Box Icon"
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
            Modifier
                .padding(8.dp)
                .weight(1f)
                .background(LocalAppColors.current.container2)
                .padding(8.dp)
        ) {
            Text("Message here....", color = LocalAppColors.current.content2)
        }
    }
}