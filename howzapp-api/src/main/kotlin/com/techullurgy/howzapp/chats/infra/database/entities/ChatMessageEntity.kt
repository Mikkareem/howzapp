package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    indexes = [
        Index(
            name = "idx_chatId_createdAt_desc",
            columnList = "chat_id,created_at DESC"
        )
    ]
)
@Inheritance(strategy = InheritanceType.JOINED)
abstract class ChatMessageEntity(
    @Id
    val id: MessageId,

    @ManyToOne
    @JoinColumn(name = "sender_id", updatable = false, nullable = false)
    val sender: UserEntity,

    @ManyToOne
    @JoinColumn(name = "chat_id", updatable = false, nullable = false)
    val belongsToChat: ChatEntity,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: Instant,
)