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
import kotlinx.coroutines.flow.map

@Composable
internal fun rememberOneStepForwardControllerForDesktop(
    backStack: SnapshotStateList<NavKey>,
): () -> Unit {
    var redoKey by remember { mutableStateOf<NavKey?>(null) }
    var prev by remember { mutableStateOf(backStack.toList()) }

    LaunchedEffect(backStack) {
        snapshotFlow { backStack.toList() }
            .map { it to prev }
            .distinctUntilChanged()
            .collect { (cur, old) ->
                redoKey = if (cur.size + 1 == old.size && cur == old.dropLast(1)) {
                    old.last()
                } else {
                    null
                }
                prev = cur
            }
    }

    return {
        redoKey?.let { key ->
            backStack.add(key)
            redoKey = null
        }
    }
}
