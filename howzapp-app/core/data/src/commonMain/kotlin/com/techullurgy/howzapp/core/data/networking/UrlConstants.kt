package com.techullurgy.howzapp.core.data.networking

import com.techullurgy.howzapp.core.data.di.HostAndPort
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.qualifier

internal expect val HOST_URL: String

object UrlConstants : KoinComponent {

    private val hostUrl by inject<String>(qualifier = qualifier<HostAndPort>())

    val BASE_URL_HTTP = "http://$hostUrl"
    val BASE_URL_WS = "ws://$hostUrl"
}