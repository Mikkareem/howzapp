package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(
            name = "idx_chat_message_receipts",
            unique = true,
            columnList = "message_id,user_id"
        )
    ]
)
data class ChatMessageReceiptsEntity(
    @Id
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "message_id")
    val message: ChatMessageEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @Enumerated(EnumType.STRING)
    val receipt: Receipt
)

enum class Receipt {
    PENDING, DELIVERED, READ
}