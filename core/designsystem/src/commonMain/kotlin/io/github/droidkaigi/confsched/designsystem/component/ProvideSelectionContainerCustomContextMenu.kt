package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.runtime.Composable

@Composable
expect fun provideSelectionContainerCustomContextMenuForDesktop(
    onWebSearchClick: (url: String) -> Unit,
    buildSearchUrl: (encodedSelectedText: String) -> String = { q ->
        "https://www.google.com/search?q=$q"
    },
    content: @Composable () -> Unit,
)
