package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.core_features.maps.source.StaticMapImage

@Composable
internal fun LocationMessageView(
    message: OriginalMessage.LocationMessage,
    onLocationMessageClick: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    StaticMapImage(
        latitude = message.latitude,
        longitude = message.longitude,
        modifier = modifier
            .clickable(
                onClick = {
                    onLocationMessageClick(message.latitude, message.longitude)
                }
            )
    )
}