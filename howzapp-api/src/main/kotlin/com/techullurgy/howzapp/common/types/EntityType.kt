package com.techullurgy.howzapp.common.types

import kotlin.uuid.Uuid

typealias UserId = String
typealias MessageId = String
typealias MessageStatusId = String
typealias ReceiptId = String
typealias ChatId = String

val String.Companion.id get() = Uuid.random().toString()