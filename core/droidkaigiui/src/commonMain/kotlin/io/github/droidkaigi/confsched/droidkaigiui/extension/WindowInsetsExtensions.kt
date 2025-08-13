package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only

fun WindowInsets.excludeTop(): WindowInsets {
    return this.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
}

/**
 * `WindowInsets()` works in IDE and compiles on Android,
 * but fails to compile on the JVM target due to overload resolution ambiguity.
 * This defines an explicit empty WindowInsets as a workaround.
 */
val WindowInsets.Companion.Empty: WindowInsets
    get() = WindowInsets(
        left = 0,
        top = 0,
        right = 0,
        bottom = 0,
    )
