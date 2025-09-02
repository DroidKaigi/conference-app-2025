package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.enableMouseDragScroll(scrollableState: ScrollableState): Modifier
