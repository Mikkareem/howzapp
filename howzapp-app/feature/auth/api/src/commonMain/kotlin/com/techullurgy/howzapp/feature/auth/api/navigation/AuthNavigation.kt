package com.techullurgy.howzapp.feature.auth.api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.techullurgy.howzapp.core.utils.inject
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

@Serializable
private data object RegisterRoute

@Serializable
private data object ForgotPasswordRoute


fun EntryProviderScope<Any>.authGraph(
    backStack: SnapshotStateList<Any>,
    onLoginSuccess: () -> Unit
) {
    entry<LoginRoute> {
        LoginScreenRoot(
            onLoginSuccess = onLoginSuccess,
            onForgotPasswordClick = {},
            onCreateAccountClick = {
                backStack.add(RegisterRoute)
            }
        )
    }

    entry<RegisterRoute> {
        RegisterScreenRoot(
            onRegisterSuccess = {
                backStack.add(LoginRoute)
            },
            onLoginClick = {
                backStack.add(LoginRoute)
            }
        )
    }
}

@Composable
private fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    inject<ILoginScreen>().invoke(
        onLoginSuccess = onLoginSuccess,
        onForgotPasswordClick = onForgotPasswordClick,
        onCreateAccountClick = onCreateAccountClick
    )
}

@Composable
private fun RegisterScreenRoot(
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    inject<IRegisterScreen>().invoke(
        onRegisterSuccess = onRegisterSuccess,
        onLoginClick = onLoginClick
    )
}