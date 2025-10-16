package com.techullurgy.howzapp.chats.infra.database.entities.messages

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("DOCUMENT")
class DocumentMessageEntity(
    id: MessageId = MessageId.id,
    sender: UserEntity,
    belongsToChat: ChatEntity,

    @Column(nullable = false, updatable = false)
    val documentUrl: String,

    @Column(nullable = false, updatable = false)
    val documentName: String
) : ChatMessageEntity(id, sender, belongsToChat)