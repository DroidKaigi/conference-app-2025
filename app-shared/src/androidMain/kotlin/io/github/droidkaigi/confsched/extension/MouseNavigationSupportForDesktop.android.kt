package io.github.droidkaigi.confsched.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey

@Composable
internal actual fun Modifier.mouseNavigationSupportForDesktop(
    backStack: SnapshotStateList<NavKey>,
): Modifier = this // NOOP
