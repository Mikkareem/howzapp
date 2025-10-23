package com.techullurgy.howzapp.feature.auth.data.services

import com.techullurgy.howzapp.core.data.networking.post
import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import com.techullurgy.howzapp.core.domain.util.map
import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.auth.data.dto.AuthInfoSerializable
import com.techullurgy.howzapp.feature.auth.data.dto.requests.AuthLoginRequest
import com.techullurgy.howzapp.feature.auth.data.dto.requests.AuthRefreshRequest
import com.techullurgy.howzapp.feature.auth.data.dto.requests.AuthRegisterRequest
import com.techullurgy.howzapp.feature.auth.data.mappers.toDomain
import com.techullurgy.howzapp.feature.auth.domain.services.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import org.koin.core.annotation.Single

@Single(binds = [AuthService::class])
internal class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {
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
        return httpClient.post<AuthLoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = AuthLoginRequest(
                email = email,
                password = password
            )
        ).map { it.toDomain() }
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return httpClient.post<AuthRefreshRequest, Unit>(
            route = "/auth/logout",
            body = AuthRefreshRequest(refreshToken)
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = AuthRegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
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