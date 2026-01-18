package com.techullurgy.howzapp

//import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
//import com.techullurgy.howzapp.feature.auth.api.navigation.LoginRoute
//import com.techullurgy.howzapp.feature.auth.api.navigation.authGraph
//import com.techullurgy.howzapp.feature.chat.api.navigation.ChatGraphRoute
//import com.techullurgy.howzapp.feature.chat.api.navigation.chatGraph
//import com.techullurgy.howzapp.feature.splash.api.navigation.SplashRoute
//import com.techullurgy.howzapp.feature.splash.api.navigation.splashRoute
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    /* HowzAppTheme {

        val backStack = rememberSaveable { mutableStateListOf<Any>(SplashRoute) }

        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                splashRoute(
                    onSuccess = {
                        backStack.clear()
                        backStack.add(ChatGraphRoute)
                    },
                    onFailure = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    }
                )
                chatGraph(backStack)
                authGraph(
                    backStack = backStack,
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(ChatGraphRoute)
                    }
                )
            }
        )
    } */
}