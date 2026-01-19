package com.techullurgy.howzapp.core_features.maps.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.techullurgy.howzapp.core_features.api.ui.LocalGoogleMapsApiKey
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol

@Composable
fun StaticMapImage(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val apiKey = LocalGoogleMapsApiKey.current
    val mapUrl = buildStaticMapUrlFromLatLng(latitude, longitude, apiKey)
    val painter = rememberAsyncImagePainter(model = mapUrl)
    val state by painter.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().background(Color.Yellow), contentAlignment = Alignment.Center) {
        when(state) {
            AsyncImagePainter.State.Empty -> {
                Text("Empty")
            }

            is AsyncImagePainter.State.Error -> {
                Text("Error: ${(state as AsyncImagePainter.State.Error).result.throwable.message}")
            }
            is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun buildStaticMapUrlFromLatLng(
    latitude: Double,
    longitude: Double,
    apiKey: String,
    zoomLevel: Int = 14,
    requiredWidth: Int = 400,
    requiredHeight: Int = 400
): String {
    return URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = "maps.googleapis.com",
        pathSegments = listOf("maps", "api", "staticmap"),
        parameters = Parameters.build {
            append("center", "$latitude,$longitude")
            append("zoom", zoomLevel.toString())
            append("size", "${requiredWidth}x$requiredHeight")
            append("key", apiKey)
        }
    ).build().toString()
}