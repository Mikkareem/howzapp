package com.techullurgy.howzapp.chats.infra.database.entities.chats

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import org.hibernate.annotations.DynamicUpdate
import kotlin.uuid.Uuid

@Entity
@DiscriminatorValue("GROUP")
@DynamicUpdate
class GroupChatEntity(
    id: ChatId = Uuid.random().toString(),
    originator: UserEntity,
    participants: List<UserEntity>,

    var title: String,
    var profilePictureUrl: String = ""
) : ChatEntity(id, originator, participants + originator) {
    init {
        type = "GROUP"
    }
}
