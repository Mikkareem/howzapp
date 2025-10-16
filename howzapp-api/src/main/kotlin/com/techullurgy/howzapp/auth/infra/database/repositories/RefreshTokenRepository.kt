package com.techullurgy.howzapp.auth.infra.database.repositories

import com.techullurgy.howzapp.auth.infra.database.entities.RefreshTokenEntity
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByUserIdAndHashedToken(userId: UserId, hashedToken: String): RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: UserId, hashedToken: String)
    fun deleteByUserId(userId: UserId)
}