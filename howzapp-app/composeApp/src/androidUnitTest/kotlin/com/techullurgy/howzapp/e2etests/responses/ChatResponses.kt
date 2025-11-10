package com.techullurgy.howzapp.e2etests.responses

import com.techullurgy.howzapp.core.dto.models.AuthInfoSerializable
import com.techullurgy.howzapp.core.dto.models.ChatDto
import com.techullurgy.howzapp.core.dto.models.ChatMessageDto
import com.techullurgy.howzapp.core.dto.models.DirectChatDto
import com.techullurgy.howzapp.core.dto.models.MessageStatusDto
import com.techullurgy.howzapp.core.dto.models.ReceiptDto
import com.techullurgy.howzapp.core.dto.models.TextMessageDto
import com.techullurgy.howzapp.core.dto.models.UserDto
import com.techullurgy.howzapp.core.dto.responses.SyncResponse
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object AppResponses {
    private const val LOGGED_IN_USERID = "9a8sd7a9s8d9a8sd79a8sd67as5da8das8d"
    private const val OTHER_USERID = "a87s6da9sda0s9das98d7a9s8d79a8sd79s"

    val loggedInUser = UserDto(LOGGED_IN_USERID, "Irsath", "")
    val otherUser = UserDto(OTHER_USERID, "Riyas", "")

    private val directChat = ChatDto(
        chatType = DirectChatDto(
            chatId = listOf(loggedInUser.userId, otherUser.userId).sorted().joinToString("__"),
            participant1 = UserDto(loggedInUser.userId, "Irsath", ""),
            participant2 = UserDto(otherUser.userId, "Riyas", "")
        ),
        messages = listOf(
            ChatMessageDto(
                messageId = "asda0s9dosada0s98da09sd809as8d",
                chatId = listOf(loggedInUser.userId, otherUser.userId).sorted().joinToString("__"),
                message = TextMessageDto("Who are you?"),
                sender = UserDto(userId = loggedInUser.userId, name = "Irsath", ""),
                status = MessageStatusDto.SENT,
                receipt = null,
                timestamp = Clock.System.now().minus(5.minutes)
            ),
            ChatMessageDto(
                messageId = "asda0s9dosada0sasdasd87as7d7as",
                chatId = listOf(loggedInUser.userId, otherUser.userId).sorted().joinToString("__"),
                message = TextMessageDto("I am Riyas"),
                sender = UserDto(userId = otherUser.userId, name = "Riyas", ""),
                status = null,
                receipt = ReceiptDto.PENDING,
                timestamp = Clock.System.now().minus(3.minutes)
            )
        )
    )

    val loginResponse = AuthInfoSerializable(
        accessToken = "aisudiausdy",
        refreshToken = "kajsdhkajsdh",
        id = loggedInUser.userId
    )

    val syncResponse = SyncResponse(
        chats = listOf(directChat),
        lastSyncTimestamp = Clock.System.now().toEpochMilliseconds()
    )
}