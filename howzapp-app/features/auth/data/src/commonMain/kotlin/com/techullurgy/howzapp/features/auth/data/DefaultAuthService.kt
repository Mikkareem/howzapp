package com.techullurgy.howzapp.features.auth.data

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import com.techullurgy.howzapp.common.utils.models.EmptyResult
import com.techullurgy.howzapp.common.utils.models.map
import com.techullurgy.howzapp.common.utils.models.onSuccess
import com.techullurgy.howzapp.core.data.api.AppHttpConnector
import com.techullurgy.howzapp.core.session.SessionNotifier
import com.techullurgy.howzapp.features.auth.data.dto.AuthInfoDto
import com.techullurgy.howzapp.features.auth.data.mappers.toDomain
import com.techullurgy.howzapp.features.auth.data.mappers.toSessionInfo
import com.techullurgy.howzapp.features.auth.data.requests.AuthLoginRequest
import com.techullurgy.howzapp.features.auth.data.requests.AuthRefreshRequest
import com.techullurgy.howzapp.features.auth.data.requests.AuthRegisterRequest
import com.techullurgy.howzapp.features.auth.domain.AuthService
import com.techullurgy.howzapp.features.auth.models.AuthInfo
import org.koin.core.annotation.Single

@Single
internal class DefaultAuthService(
    private val client: AppHttpConnector,
    private val sessionNotifier: SessionNotifier
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
        return client.post<AuthLoginRequest, AuthInfoDto>(
            route = "/api/auth/login",
            body = AuthLoginRequest(
                email = email,
                password = password
            )
        )
            .map { it.toDomain() }
            .onSuccess {
                sessionNotifier.setSessionInfo(it.toSessionInfo())
            }
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return client.post<AuthRefreshRequest, Unit>(
            route = "/api/auth/logout",
            body = AuthRefreshRequest(
                refreshToken = refreshToken
            )
        ).onSuccess {
            client.clearAuthToken()
            sessionNotifier.setSessionInfo(null)
        }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return client.post<AuthRegisterRequest, Unit>(
            route = "/api/auth/register",
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