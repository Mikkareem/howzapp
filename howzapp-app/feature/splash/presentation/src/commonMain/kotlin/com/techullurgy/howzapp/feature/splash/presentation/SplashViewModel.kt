package com.techullurgy.howzapp.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.feature.splash.domain.usecases.VerifyLoginUsecase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SplashViewModel(
    private val verifyLogin: VerifyLoginUsecase
) : ViewModel() {
    private val _events = Channel<Boolean>()

    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(5000)
            _events.send(verifyLogin())
        }
    }
}