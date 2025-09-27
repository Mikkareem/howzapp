package com.techullurgy.howzapp.users.infra.mappers

import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.models.AppUser

fun UserEntity.toDomain(): AppUser = AppUser(
    id = id,
    name = name,
    profilePictureUrl = profilePictureUrl
)

fun AppUser.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    profilePictureUrl = profilePictureUrl
)