package com.techullurgy.howzapp.core.network

import io.ktor.websocket.Frame as KtorFrame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

sealed interface Frame {
    data class Text(val text: String) : Frame
    class Ping(val data: ByteArray) : Frame
    class Pong(val data: ByteArray) : Frame
    data object Close : Frame
}

interface WebsocketSession {
    val isActive: Boolean

    val incoming: Flow<Frame>

    suspend fun send(frame: Frame)

    suspend fun send(content: String)

    suspend fun close()
}

internal class KtorWebsocketSession(
    private val session: WebSocketSession
) : WebsocketSession {
    override val isActive get() = session.isActive

    override val incoming = session.incoming
        .receiveAsFlow()
        .map {
            when (it) {
                is KtorFrame.Close -> Frame.Close
                is KtorFrame.Ping -> Frame.Ping(it.data)
                is KtorFrame.Pong -> Frame.Pong(it.data)
                is KtorFrame.Text -> Frame.Text(it.readText())
                else -> throw Exception("Unknown Frame Type")
            }
        }
        .catch {
            throw it.transformKtorThrowable()
        }

    override suspend fun send(frame: Frame) {
        val wsFrame = when (frame) {
            is Frame.Close -> KtorFrame.Close()
            is Frame.Ping -> KtorFrame.Ping(frame.data)
            is Frame.Pong -> KtorFrame.Pong(frame.data)
            is Frame.Text -> KtorFrame.Text(frame.text)
        }
        session.send(wsFrame)
    }

    override suspend fun send(content: String) {
        session.send(content)
    }

    override suspend fun close() {
        session.close()
    }
}