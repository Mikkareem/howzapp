package com.techullurgy.howzapp.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun AppButton(
    text: String,
    numbers: () -> List<Int>
) {

}

@Preview(showBackground = true, backgroundColor = 0xFFEAA33F)
@Composable
private fun AppButtonPreview() {
    AppButton(
        text = "Irsath",
        numbers = { listOf(1,2,3,4,5,6,7,8) }
    )
}