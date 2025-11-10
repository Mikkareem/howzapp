package com.techullurgy.howzapp.users.infra.database.entities

import com.techullurgy.howzapp.common.types.UserId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
data class UserEntity(
    @Id
    val id: UserId,
    val name: String,
    val email: String,
    val hashedPassword: String,
    val profilePictureUrl: String,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Instant = Instant.now()
) {
    @UpdateTimestamp
    lateinit var updatedAt: Instant
}