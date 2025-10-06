package com.techullurgy.howzapp.feature.chat.database.models

import com.techullurgy.howzapp.feature.chat.database.utils.ChatTypeConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableChatType {

    @Serializable
    @SerialName(ChatTypeConstants.DISCRIMINATOR_DIRECT_CHAT)
    data class Direct(
        val meId: String,
        val otherId: String,
    ): SerializableChatType

    @Serializable
    @SerialName(ChatTypeConstants.DISCRIMINATOR_GROUP_CHAT)
    data class Group(
        val title: String,
        val profileUrl: String? = null
    ): SerializableChatType
}