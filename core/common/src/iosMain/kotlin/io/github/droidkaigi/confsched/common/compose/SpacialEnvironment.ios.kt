package io.github.droidkaigi.confsched.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSpacialEnvironment(): SpacialEnvironment {
    return remember {
        object : SpacialEnvironment {
            override val enabledSpacialControl: Boolean = false
            override val isFullSpace: Boolean = false

            override fun toggleSpaceMode() {
            }
        }
    }
}
