package com.techullurgy.howzapp.core.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

fun Modifier.testTag(tag: TestTag) = this.then(Modifier.testTag(tag.name))

class TestTag internal constructor(val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestTag) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {}
}

val TestTag.Companion.loginEmailLabel: TestTag
    get() = TestTag("loginEmailLabel")

val TestTag.Companion.loginEmailInput: TestTag
    get() = TestTag("loginEmailInput")

val TestTag.Companion.loginPasswordLabel: TestTag
    get() = TestTag("loginPasswordLabel")

val TestTag.Companion.loginPasswordInput: TestTag
    get() = TestTag("loginPasswordInput")

fun TestTag.Companion.chatMessage(chatId: String, messageId: String): TestTag =
    TestTag("chatMessage:$chatId:$messageId")

val TestTag.Companion.messageInput: TestTag
    get() = TestTag("messageInput")
