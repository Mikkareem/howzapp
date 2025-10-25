package com.techullurgy.howzapp.core.data.networking

expect val HOST_URL: String

object UrlConstants {

    const val PORT = 8080

    val BASE_URL_HTTP = "http://$HOST_URL:$PORT"
    val BASE_URL_WS = "ws://$HOST_URL:$PORT"
}