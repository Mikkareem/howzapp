package com.techullurgy.howzapp.feature.auth.presentation.utils

internal object PasswordValidator {

    private const val MIN_PASSWORD_LENGTH = 9

    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
            hasDigit = password.any { it.isDigit() },
            hasUppercase = password.any { it.isUpperCase() }
        )
    }
}

internal data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasUppercase: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasDigit && hasUppercase
}