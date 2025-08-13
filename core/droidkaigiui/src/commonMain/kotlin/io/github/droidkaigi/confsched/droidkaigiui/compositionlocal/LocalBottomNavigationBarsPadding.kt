package io.github.droidkaigi.confsched.droidkaigiui.compositionlocal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal to provide extra padding for the bottom navigation bar.
 * This prevents the bottom navigation bar from overlapping the content.
 *
 * The value already includes the navigationBars padding from [WindowInsets].
 * If the screen displays a bottom navigation bar, use [safeDrawingWithBottomNavBar]
 * instead of [safeDrawing] to avoid applying the navigationBars padding from [WindowInsets] more than once.
 */
val LocalBottomNavigationBarsPadding = staticCompositionLocalOf {
    PaddingValues()
}

val WindowInsets.Companion.safeDrawingWithBottomNavBar: WindowInsets
    @Composable
    get() = WindowInsets.safeDrawing.union(
        WindowInsets(
            bottom = LocalBottomNavigationBarsPadding.current.calculateBottomPadding()
        )
    )
