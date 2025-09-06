package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Modifier.enableMouseDragScroll(
    scrollableState: ScrollableState,
): Modifier = this // NOOP

@Composable
actual fun Modifier.bindMouseBackForward(
    onBackPressed: () -> Unit,
    onForwardPressed: () -> Unit,
): Modifier = this // NOOP

@Composable
actual fun Modifier.enableMouseWheelZoomForDesktop(
    multiplyVerticalScaleBy: (Float) -> Unit,
    zoomStep: Float,
    requireModifierKey: Boolean,
): Modifier = this // NOOP
