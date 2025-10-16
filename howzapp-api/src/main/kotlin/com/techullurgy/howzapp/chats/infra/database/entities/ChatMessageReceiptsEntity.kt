package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

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

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false, updatable = false)
    val message: ChatMessageEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", updatable = false)
    val user: UserEntity,

    @Enumerated(EnumType.ORDINAL)
    val receipt: Receipt,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
) {

    @UpdateTimestamp
    @Column(name = "updated_at")
    lateinit var updatedAt: Instant
}

enum class Receipt {
    PENDING, DELIVERED, READ
}