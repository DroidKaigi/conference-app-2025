package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.enableMouseDragScroll(
    scrollableState: ScrollableState,
): Modifier

@Composable
expect fun Modifier.enableMouseWheelZoomForDesktop(
    multiplyVerticalScaleBy: (Float) -> Unit,
    zoomStep: Float = 1.05f,
    requireModifierKey: Boolean = true,
): Modifier
