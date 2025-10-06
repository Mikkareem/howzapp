package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableOnlineStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OnlineStatus
import kotlin.time.Instant

fun SerializableOnlineStatus.toDomain(): OnlineStatus {
    return when(this) {
        SerializableOnlineStatus.Online -> OnlineStatus.IsOnline
        SerializableOnlineStatus.NoOnlineStatus -> OnlineStatus.NoOnlineStatus
        is SerializableOnlineStatus.Offline -> OnlineStatus.NotInOnline(Instant.fromEpochMilliseconds(lastSeen))
    }
}

fun OnlineStatus.toSerializable(): SerializableOnlineStatus {
    return when(this) {
        OnlineStatus.IsOnline -> SerializableOnlineStatus.Online
        OnlineStatus.NoOnlineStatus -> SerializableOnlineStatus.NoOnlineStatus
        is OnlineStatus.NotInOnline -> SerializableOnlineStatus.Offline(lastSeen.toEpochMilliseconds())
    }
}