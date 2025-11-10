package com.techullurgy.howzapp.users.infra.database.repositories

import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, UserId> {
    fun findByEmailOrName(email: String, name: UserId): UserEntity?
    fun findByEmail(email: String): UserEntity?
}