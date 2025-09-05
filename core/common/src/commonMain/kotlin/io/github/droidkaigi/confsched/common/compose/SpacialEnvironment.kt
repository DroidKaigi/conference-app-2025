package io.github.droidkaigi.confsched.common.compose

import androidx.compose.runtime.Composable

@Composable
expect fun rememberSpacialEnvironment(): SpacialEnvironment

interface SpacialEnvironment {
    val enabledSpacialControl: Boolean
    val isFullSpace: Boolean
    fun toggleSpaceMode()
}
