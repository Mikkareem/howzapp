package com.techullurgy.howzapp.core.data.auth

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.AuthService
import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun login(
        email: String,
        password: String
    ): AppResult<AuthInfo, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }
}