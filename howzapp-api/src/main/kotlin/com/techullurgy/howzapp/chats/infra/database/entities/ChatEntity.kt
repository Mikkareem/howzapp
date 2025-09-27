package com.techullurgy.howzapp.chats.infra.database.entities

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
    name = "type",
    discriminatorType = DiscriminatorType.STRING
)
abstract class ChatEntity(
    @Id val id: ChatId,

    @ManyToOne
    @JoinColumn("originator_id", updatable = false)
    val originator: UserEntity,

    @ManyToMany
    @JoinTable(
        name = "chat_participants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val participants: List<UserEntity>
) {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    @Column(nullable = false, insertable = false, updatable = false)
    var type: String? = null
        protected set
}