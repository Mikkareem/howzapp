package com.techullurgy.howzapp.chats.infra.database.entities.messages

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
    name = "type",
    discriminatorType = DiscriminatorType.STRING
)
abstract class MediaMessageEntity(
    id: MessageId = MessageId.id,
    sender: UserEntity,
    belongsToChat: ChatEntity,

    @Column(nullable = true, updatable = false)
    val mediaUrl: String?
) : ChatMessageEntity(id, sender, belongsToChat)