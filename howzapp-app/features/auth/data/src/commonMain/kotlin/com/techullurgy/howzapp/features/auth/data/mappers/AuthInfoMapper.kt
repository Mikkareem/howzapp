package com.techullurgy.howzapp.features.auth.data.mappers

import com.techullurgy.howzapp.core.session.SessionInfo
import com.techullurgy.howzapp.features.auth.data.dto.AuthInfoDto
import com.techullurgy.howzapp.features.auth.models.AuthInfo

internal fun AuthInfoDto.toDomain(): AuthInfo = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    id = id
)

internal fun AuthInfo.toSessionInfo(): SessionInfo = SessionInfo(
    id = id,
    accessToken = accessToken,
    refreshToken = refreshToken
)