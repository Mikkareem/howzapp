package com.techullurgy.howzapp.core.data.networking

import com.techullurgy.howzapp.core.data.HowzappKonfig

object UrlConstants {
    private const val HOST_URL = "${HowzappKonfig.SERVER_HOST}:${HowzappKonfig.SERVER_PORT}"

    const val BASE_URL_HTTP = "http://$HOST_URL"
    const val BASE_URL_WS = "ws://$HOST_URL"
}