package com.techullurgy.howzapp.core.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

class TestTag internal constructor(val name: String) {
    companion object
}

val TestTag.Companion.loginEmailLabel: TestTag
    get() = TestTag("loginEmailLabel")

val TestTag.Companion.loginEmailInput: TestTag
    get() = TestTag("loginEmailInput")

val TestTag.Companion.loginPasswordLabel: TestTag
    get() = TestTag("loginPasswordLabel")

val TestTag.Companion.loginPasswordInput: TestTag
    get() = TestTag("loginPasswordInput")

fun Modifier.testTag(tag: TestTag) = this.then(Modifier.testTag(tag.name))