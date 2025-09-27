package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.common.types.MessageStatusId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
class ChatMessageStatusEntity(
    @Id val id: MessageStatusId = MessageStatusId.id,

    @OneToOne
    @JoinColumn("message_id")
    val message: ChatMessageEntity,

    @ManyToOne
    @JoinColumn("sender_id")
    val sender: UserEntity,

    @Enumerated(value = EnumType.STRING)
    val status: MessageStatus,

    val intendedCount: Int = 1,

    val currentCount: Int = 0
)

enum class MessageStatus {
    SENT, RECEIVED, READ
}