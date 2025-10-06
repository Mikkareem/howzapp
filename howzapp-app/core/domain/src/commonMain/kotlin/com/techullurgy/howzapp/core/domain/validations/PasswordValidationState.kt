package com.techullurgy.howzapp.core.domain.validations

data class PasswordValidationState(
    val hasMinLength: Boolean,
    val hasDigit: Boolean,
    val hasUppercase: Boolean
) {
    val isValidPassword = hasMinLength && hasDigit && hasUppercase
}
