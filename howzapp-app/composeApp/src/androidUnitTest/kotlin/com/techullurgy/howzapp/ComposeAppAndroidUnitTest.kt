package com.techullurgy.howzapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.techullurgy.howzapp.feature.chat.test.di.chatTestDomainModule
import com.techullurgy.howzapp.test.utilities.RobolectricTest
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.email
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.robolectric.RuntimeEnvironment
import kotlin.test.Test

class ComposeAppAndroidUnitTest : KoinTest, RobolectricTest() {

    @get:Rule
    val koin = KoinTestRule.create {
        androidContext(RuntimeEnvironment.getApplication())
        modules(DevelopmentApp.configurationModules + chatTestDomainModule)
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun composeTest() = runTest {
        composeRule.setContent { App() }
        val emailString = getString(Res.string.email)
        composeRule.onNodeWithText(emailString).assertExists()
    }
}