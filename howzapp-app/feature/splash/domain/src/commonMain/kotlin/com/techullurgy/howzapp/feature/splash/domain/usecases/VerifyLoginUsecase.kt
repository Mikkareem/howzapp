package com.techullurgy.howzapp.feature.splash.domain.usecases

import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class VerifyLoginUsecase(
    private val sessionStorage: SessionStorage
) {
    suspend operator fun invoke(): Boolean {
        return sessionStorage.observeAuthInfo()
            .map {
                it != null
            }.first()
    }
}