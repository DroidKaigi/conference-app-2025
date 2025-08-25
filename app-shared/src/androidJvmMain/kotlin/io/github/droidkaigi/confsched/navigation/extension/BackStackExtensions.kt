package io.github.droidkaigi.confsched.navigation.extension

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

/**
 * Safely removes the last item from the backstack.
 * Prevents "java.lang.IllegalArgumentException: NavDisplay backstack cannot be empty"
 * when the system back button and UI back button are pressed simultaneously in navigation3.
 */
fun SnapshotStateList<NavKey>.safeRemoveLastOrNull(): NavKey? {
    return if (size > 1) {
        removeLastOrNull()
    } else {
        null
    }
}
