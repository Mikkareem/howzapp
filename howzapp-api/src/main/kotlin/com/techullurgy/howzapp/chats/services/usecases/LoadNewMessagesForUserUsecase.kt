package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.chats.models.Chat
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Component
class LoadNewMessagesForUserUsecase(
    private val userRepository: UserRepository,
    private val messageRepository: ChatMessageRepository
) {
    operator fun invoke(
        userId: UserId,
        after: Instant
    ): List<Chat> {
        val user = userRepository.findById(userId).getOrNull() ?: return emptyList()
        val fetchedMessages = messageRepository.fetchNewMessagesForUser(user, after)

        val chats = fetchedMessages.groupBy {
            it.message.belongsToChat
        }.map { map ->
            Chat(
                map.key.toDomain(),
                map.value.map { it.toDomain() }
            )
        }

        return chats
    }
}