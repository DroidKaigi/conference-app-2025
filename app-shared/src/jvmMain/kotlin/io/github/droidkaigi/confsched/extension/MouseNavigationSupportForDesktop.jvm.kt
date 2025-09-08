package io.github.droidkaigi.confsched.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isForwardPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.navigation3.runtime.NavKey
import io.github.droidkaigi.confsched.component.rememberOneStepForwardControllerForDesktop
import io.github.droidkaigi.confsched.navkey.TimetableNavKey

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun Modifier.mouseNavigationSupportForDesktop(
    backStack: SnapshotStateList<NavKey>,
): Modifier {
    val doForward = rememberOneStepForwardControllerForDesktop(backStack)

    return this.onPointerEvent(
        eventType = PointerEventType.Press,
        pass = PointerEventPass.Initial,
    ) { e ->
        when {
            e.buttons.isBackPressed -> {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                } else {
                    backStack.clear()
                    backStack.add(TimetableNavKey)
                }
                e.changes.forEach { it.consume() }
            }
            e.buttons.isForwardPressed -> {
                doForward()
                e.changes.forEach { it.consume() }
            }
        }
    }
}
