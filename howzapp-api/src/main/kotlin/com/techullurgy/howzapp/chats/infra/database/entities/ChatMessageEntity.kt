package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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
    val createdAt: Instant = Instant.now(),

    val isDeleted: Boolean = false
) {
    @UpdateTimestamp
    @Column(name = "updated_at")
    lateinit var updatedAt: Instant

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChatMessageEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}