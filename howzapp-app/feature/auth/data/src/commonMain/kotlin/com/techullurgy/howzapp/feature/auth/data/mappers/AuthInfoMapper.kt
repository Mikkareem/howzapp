package com.techullurgy.howzapp.feature.auth.data.mappers

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.dto.models.AuthInfoSerializable

fun AuthInfoSerializable.toDomain(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        id = id
    )
}