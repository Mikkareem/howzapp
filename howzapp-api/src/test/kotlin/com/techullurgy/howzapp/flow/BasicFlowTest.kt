package com.techullurgy.howzapp.flow

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.util.MultiValueMap
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.File
import java.net.URI
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BasicFlowTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var mockMvcTester: MockMvcTester

    private val websocketClient = StandardWebSocketClient()


    @Test
    fun `test new message, send for new user, first time`() {
        val messagesForUser1 = ArrayBlockingQueue<String>(1)
        val messagesForUser2 = ArrayBlockingQueue<String>(1)

        val user1 = mockMvcTester.post()
            .uri("/users/user/{name}", "Irsath")
            .exchange()
            .response.contentAsString.removePrefix("Created ")

        val user2 = mockMvcTester.post()
            .uri("/users/user/{name}", "Kareem")
            .exchange()
            .response.contentAsString.removePrefix("Created ")

        val handler1 = object : AbstractWebSocketHandler() {
            override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
                messagesForUser1.offer(message.payload)
            }
        }

        val handler2 = object : AbstractWebSocketHandler() {
            override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
                messagesForUser2.offer(message.payload)
            }
        }

        val websocketHeader1 = WebSocketHttpHeaders(
            HttpHeaders(MultiValueMap.fromSingleValue(mapOf("x-user" to user1)))
        )
        val websocketHeader2 = WebSocketHttpHeaders(
            HttpHeaders(MultiValueMap.fromSingleValue(mapOf("x-user" to user2)))
        )

        websocketClient.execute(
            handler1,
            websocketHeader1,
            URI.create("ws://localhost:$port/ws")
        ).get()

        websocketClient.execute(
            handler2,
            websocketHeader2,
            URI.create("ws://localhost:$port/ws")
        ).get()

        File("").length()

        mockMvcTester.get()
            .uri("/rabbit/send/random")
            .exchange()

        val receivedMessage1 = messagesForUser1.poll(10, TimeUnit.SECONDS)
        val receivedMessage2 = messagesForUser1.poll(10, TimeUnit.SECONDS)

        println(receivedMessage1)
        println(receivedMessage2)
    }
}