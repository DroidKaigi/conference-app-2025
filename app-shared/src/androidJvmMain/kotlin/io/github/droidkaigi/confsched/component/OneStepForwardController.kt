package io.github.droidkaigi.confsched.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun rememberOneStepForwardControllerForDesktop(
    backStack: SnapshotStateList<NavKey>,
): () -> Unit {
    var redoKey by remember { mutableStateOf<NavKey?>(null) }

    LaunchedEffect(backStack) {
        var previous = backStack.toList()

        snapshotFlow { backStack.toList() }
            .distinctUntilChanged()
            .collect { current ->
                redoKey = if (current == previous.dropLast(1)) {
                    previous.last()
                } else {
                    null
                }
                previous = current
            }
    }

    return {
        redoKey?.let { key ->
            backStack.add(key)
            redoKey = null
        }
    }
}
