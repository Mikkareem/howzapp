package com.techullurgy.howzapp.chats.infra.database.entities.chats

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kotlin.uuid.Uuid

@Entity
@DiscriminatorValue("ONE_TO_ONE")
class OneToOneChatEntity(
    id: ChatId = Uuid.random().toString(),
    originator: UserEntity,
    participant: UserEntity
): ChatEntity(id, originator, listOf(originator, participant)) {
    init {
        type = "ONE_TO_ONE"
    }
}