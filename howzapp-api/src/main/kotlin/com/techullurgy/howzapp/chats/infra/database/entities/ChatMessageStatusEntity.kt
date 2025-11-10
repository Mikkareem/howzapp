package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.common.types.MessageStatusId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(
    indexes = [
        Index(
            name = "idx_message_user_unique",
            columnList = "message_id,sender_id",
            unique = true
        )
    ]
)
class ChatMessageStatusEntity(
    @Id val id: MessageStatusId = MessageStatusId.id,

    @OneToOne
    @JoinColumn("message_id")
    val message: ChatMessageEntity,

    @ManyToOne
    @JoinColumn(name = "sender_id")
    val sender: UserEntity,

    @Enumerated(value = EnumType.STRING)
    val status: MessageStatus,

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: Instant = Instant.now()
) {
    @UpdateTimestamp
    @Column(name = "updated_at")
    lateinit var updatedAt: Instant
}

enum class MessageStatus {
    SENT, RECEIVED, READ
}