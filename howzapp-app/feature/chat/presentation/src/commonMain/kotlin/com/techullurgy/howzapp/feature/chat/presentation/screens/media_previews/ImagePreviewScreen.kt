package com.techullurgy.howzapp.feature.chat.presentation.screens.media_previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.feature.chat.api.navigation.IImagePreviewScreen
import org.koin.core.annotation.Factory

@Factory(binds = [IImagePreviewScreen::class])
internal class DefaultIImagePreviewScreen : IImagePreviewScreen {
    @Composable
    override fun invoke(url: String) {
        ImagePreviewScreen(url = url)
    }
}

@Composable
private fun ImagePreviewScreen(
    url: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}