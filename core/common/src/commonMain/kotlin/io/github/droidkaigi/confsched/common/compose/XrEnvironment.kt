package io.github.droidkaigi.confsched.common.compose

import androidx.compose.runtime.Composable

@Composable
expect fun rememberXrEnvironment(): XrEnvironment

interface XrEnvironment {
    val enabledSpacialControl: Boolean
    val isFullSpace: Boolean
    fun toggleSpaceMode()
}
