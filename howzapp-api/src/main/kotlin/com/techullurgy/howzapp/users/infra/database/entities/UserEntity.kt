package com.techullurgy.howzapp.users.infra.database.entities

import com.techullurgy.howzapp.common.types.UserId
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class UserEntity(
    @Id
    val id: UserId,
    val name: String,
    val profilePictureUrl: String
)