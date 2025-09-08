package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.sessions.grid.TimetableGridScope

@Composable
expect fun TimetableGridScope.ContextMenuProviderForDesktop(
    isBookmarked: Boolean,
    onSelectShowDetail: () -> Unit,
    onToggleFavorite: () -> Unit,
    content: @Composable () -> Unit,
)
