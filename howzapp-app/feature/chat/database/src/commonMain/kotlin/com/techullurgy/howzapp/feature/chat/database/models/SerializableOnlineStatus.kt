package com.techullurgy.howzapp.feature.chat.database.models

import com.techullurgy.howzapp.feature.chat.database.utils.OnlineStatusConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableOnlineStatus {

    @Serializable
    @SerialName(OnlineStatusConstants.DISCRIMINATOR_NO_ONLINE_STATUS)
    data object NoOnlineStatus: SerializableOnlineStatus

    @Serializable
    @SerialName(OnlineStatusConstants.DISCRIMINATOR_ONLINE)
    data object Online: SerializableOnlineStatus

    @Serializable
    @SerialName(OnlineStatusConstants.DISCRIMINATOR_OFFLINE)
    data class Offline(val lastSeen: Long): SerializableOnlineStatus
}