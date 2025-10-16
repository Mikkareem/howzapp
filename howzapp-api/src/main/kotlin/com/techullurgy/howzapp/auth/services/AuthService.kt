package com.techullurgy.howzapp.auth.services

import com.techullurgy.howzapp.auth.infra.database.entities.RefreshTokenEntity
import com.techullurgy.howzapp.auth.infra.database.repositories.RefreshTokenRepository
import com.techullurgy.howzapp.common.exceptions.InvalidCredentialsException
import com.techullurgy.howzapp.common.exceptions.InvalidTokenException
import com.techullurgy.howzapp.common.exceptions.UserAlreadyExistsException
import com.techullurgy.howzapp.common.exceptions.UserNotFoundException
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import com.techullurgy.howzapp.users.infra.mappers.toDomain
import com.techullurgy.howzapp.users.models.AppUser
import com.techullurgy.howzapp.users.models.AuthenticatedUser
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import kotlin.uuid.Uuid

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    @Transactional
    fun register(
        email: String,
        username: String,
        password: String
    ): AppUser {
        val trimmedEmail = email.trim()

        val user = userRepository.findByEmailOrName(
            email = trimmedEmail,
            name = username.trim()
        )

        if (user != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.saveAndFlush(
            UserEntity(
                id = Uuid.id.toString(),
                name = username.trim(),
                email = trimmedEmail,
                hashedPassword = passwordEncoder.encode(password)!!,
                profilePictureUrl = ""
            )
        ).toDomain()

        return savedUser
    }

    @Transactional
    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        return user.id.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                id = user.id,
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    @Transactional
    fun refresh(refreshToken: String): AuthenticatedUser {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                message = "Invalid refresh token"
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)

        return user.id.let { userId ->
            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            ) ?: throw InvalidTokenException("Invalid refresh token")

            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            )

            val newAccessToken = jwtService.generateAccessToken(userId)
            val newRefreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, newRefreshToken)

            AuthenticatedUser(
                id = userId,
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        }
    }

    @Transactional
    fun logout(refreshToken: String) {
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val hashed = hashToken(refreshToken)
        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashed)
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashed = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}