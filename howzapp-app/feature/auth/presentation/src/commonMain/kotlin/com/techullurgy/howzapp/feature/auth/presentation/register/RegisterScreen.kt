package com.techullurgy.howzapp.feature.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.howzapp.core.designsystem.components.brand.HowzappBrandLogo
import com.techullurgy.howzapp.core.designsystem.components.buttons.HowzappButton
import com.techullurgy.howzapp.core.designsystem.components.buttons.HowzappButtonStyle
import com.techullurgy.howzapp.core.designsystem.components.layouts.HowzappAdaptiveFormLayout
import com.techullurgy.howzapp.core.designsystem.components.layouts.HowzappSnackbarScaffold
import com.techullurgy.howzapp.core.designsystem.components.textfields.HowzappPasswordTextField
import com.techullurgy.howzapp.core.designsystem.components.textfields.HowzappTextField
import com.techullurgy.howzapp.core.presentation.util.ObserveAsEvents
import howzapp.feature.auth.presentation.generated.resources.Res
import howzapp.feature.auth.presentation.generated.resources.email
import howzapp.feature.auth.presentation.generated.resources.email_placeholder
import howzapp.feature.auth.presentation.generated.resources.login
import howzapp.feature.auth.presentation.generated.resources.password
import howzapp.feature.auth.presentation.generated.resources.password_hint
import howzapp.feature.auth.presentation.generated.resources.register
import howzapp.feature.auth.presentation.generated.resources.username
import howzapp.feature.auth.presentation.generated.resources.username_hint
import howzapp.feature.auth.presentation.generated.resources.username_placeholder
import howzapp.feature.auth.presentation.generated.resources.welcome_to_chirp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is RegisterEvent.Success -> {
                onRegisterSuccess(event.email)
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    HowzappSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        HowzappAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_to_chirp),
            errorText = state.registrationError?.asString(),
            logo = { HowzappBrandLogo() }
        ) {
            HowzappTextField(
                state = state.usernameTextState,
                placeholder = stringResource(Res.string.username_placeholder),
                title = stringResource(Res.string.username),
                supportingText = state.usernameError?.asString()
                    ?: stringResource(Res.string.username_hint),
                isError = state.usernameError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HowzappTextField(
                state = state.emailTextState,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            HowzappPasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                supportingText = state.passwordError?.asString()
                    ?: stringResource(Res.string.password_hint),
                isError = state.passwordError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                },
                onToggleVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(16.dp))

            HowzappButton(
                text = stringResource(Res.string.register),
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                enabled = state.canRegister,
                isLoading = state.isRegistering,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            HowzappButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
                style = HowzappButtonStyle.SECONDARY,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}