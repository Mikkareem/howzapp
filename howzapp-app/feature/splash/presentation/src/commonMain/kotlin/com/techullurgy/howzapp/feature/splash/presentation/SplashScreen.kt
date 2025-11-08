package com.techullurgy.howzapp.feature.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.core.designsystem.components.brand.HowzappBrandLogo
import com.techullurgy.howzapp.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
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

    SplashScreen()
}

@Preview
@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        HowzappBrandLogo()
    }
}