package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CursorDropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import io.github.droidkaigi.confsched.droidkaigiui.DroidkaigiuiRes
import io.github.droidkaigi.confsched.droidkaigiui.add_to_bookmark
import io.github.droidkaigi.confsched.droidkaigiui.remove_from_bookmark
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.go_to_timetable_detail
import io.github.droidkaigi.confsched.sessions.grid.TimetableGridScope
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun TimetableGridScope.ContextMenuProviderForDesktop(
    isBookmarked: Boolean,
    onSelectShowDetail: () -> Unit,
    onToggleFavorite: () -> Unit,
    content: @Composable () -> Unit,
) {
    val (open, setOpen) = remember { mutableStateOf(false) }
    val gotoTimetableDetailLabel = stringResource(SessionsRes.string.go_to_timetable_detail)
    val bookmarkLabel = stringResource(
        if (isBookmarked) {
            DroidkaigiuiRes.string.remove_from_bookmark
        } else {
            DroidkaigiuiRes.string.add_to_bookmark
        },
    )

    // TODO Improve the appearance.
    Box(
        Modifier.onPointerEvent(
            eventType = PointerEventType.Press,
            pass = PointerEventPass.Initial,
        ) { e ->
            if (e.buttons.isSecondaryPressed) {
                setOpen(true)
                e.changes.forEach { it.consume() }
            }
        },
    ) {
        content()

        CursorDropdownMenu(
            expanded = open,
            onDismissRequest = { setOpen(false) },
        ) {
            @Composable
            fun item(label: String, onClick: () -> Unit) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = Color.Black,
                        )
                    },
                    onClick = {
                        setOpen(false)
                        onClick()
                    },
                )
            }

            val items = listOf(
                gotoTimetableDetailLabel to onSelectShowDetail,
                bookmarkLabel to onToggleFavorite,
            )
            items.forEach { (label, action) -> item(label, action) }
        }
    }
}
