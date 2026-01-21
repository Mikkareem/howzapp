package com.techullurgy.howzapp.feature.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techullurgy.howzapp.core.designsystem.components.brand.HowzappBrandLogo
import com.techullurgy.howzapp.core.presentation.util.ObserveAsEvents
import com.techullurgy.howzapp.feature.splash.api.navigation.ISplashScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Factory

@Factory(binds = [ISplashScreen::class])
internal class DefaultISplashScreen : ISplashScreen {
    @Composable
    override fun invoke(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        SplashScreen(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}

@Composable
private fun SplashScreen(
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val viewModel = koinViewModel<SplashViewModel>()

    ObserveAsEvents(
        viewModel.events
    ) {
        if (it) {
            onSuccess()
        } else {
            onFailure()
        }
    }

    SplashScreenRoot()
}

@Preview
@Composable
private fun SplashScreenRoot() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        HowzappBrandLogo()
    }
}