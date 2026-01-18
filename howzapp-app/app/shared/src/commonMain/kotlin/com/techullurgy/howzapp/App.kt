package com.techullurgy.howzapp

//import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
//import com.techullurgy.howzapp.feature.auth.api.navigation.LoginRoute
//import com.techullurgy.howzapp.feature.auth.api.navigation.authGraph
//import com.techullurgy.howzapp.feature.chat.api.navigation.ChatGraphRoute
//import com.techullurgy.howzapp.feature.chat.api.navigation.chatGraph
//import com.techullurgy.howzapp.feature.splash.api.navigation.SplashRoute
//import com.techullurgy.howzapp.feature.splash.api.navigation.splashRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun App() {

    Box(Modifier.fillMaxSize().background(Color.Green), Alignment.Center) {
        Text("Hello World", color = Color.White, fontSize = 32.sp)
    }

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