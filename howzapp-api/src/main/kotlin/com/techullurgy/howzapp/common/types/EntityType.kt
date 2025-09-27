package com.techullurgy.howzapp.common.types

import kotlin.uuid.Uuid

typealias UserId = Uuid
typealias MessageId = Uuid
typealias MessageStatusId = Uuid
typealias ChatId = Uuid
typealias EventSourceId = Uuid

val Uuid.Companion.id get() = random()