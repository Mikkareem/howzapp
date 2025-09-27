package com.techullurgy.howzapp.chats.infra.database.entities.messages

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.time.Instant

@Entity
@DiscriminatorValue("MEDIA_GROUP")
class MediaGroupMessageEntity(
    id: MessageId = MessageId.id,
    sender: UserEntity,
    belongsToChat: ChatEntity,
    createdAt: Instant = Instant.now(),

    @OneToMany
    @JoinColumn(name = "media_id", nullable = false, updatable = false)
    val medias: List<MediaMessageEntity>
): MediaMessageEntity(id, sender, belongsToChat, createdAt, null)