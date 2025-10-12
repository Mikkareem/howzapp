package com.techullurgy.howzapp.common.types

import kotlin.uuid.Uuid

typealias UserId = String
typealias MessageId = Uuid
typealias MessageStatusId = Uuid
typealias ChatId = String
typealias EventSourceId = Uuid

val Uuid.Companion.id get() = random()