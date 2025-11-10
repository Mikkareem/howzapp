package com.techullurgy.howzapp.chats.infra.database.entities.messages

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("MEDIA_VIDEO")
class VideoMessageEntity(
    id: MessageId = MessageId.id,
    sender: UserEntity,
    belongsToChat: ChatEntity,
    videoUrl: String,
) : MediaMessageEntity(id, sender, belongsToChat, videoUrl)