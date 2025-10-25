package com.techullurgy.howzapp.core.designsystem.preview

import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

data class PreviewUiState<T>(
    val state: T,
    val isDarkMode: Boolean
)

abstract class PreviewUiStateParameterProvider<T> : PreviewParameterProvider<PreviewUiState<T>> {
    protected abstract val originalValues: List<T>

    final override val values: Sequence<PreviewUiState<T>>
        get() = (
                originalValues.map { PreviewUiState(it, true) }
                        + originalValues.map { PreviewUiState(it, false) }
                ).asSequence()
}