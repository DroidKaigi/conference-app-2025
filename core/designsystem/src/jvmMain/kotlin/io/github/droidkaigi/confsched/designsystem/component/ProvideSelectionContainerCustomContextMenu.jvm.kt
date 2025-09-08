package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import io.github.droidkaigi.confsched.designsystem.DesignsystemRes
import io.github.droidkaigi.confsched.designsystem.web_search
import org.jetbrains.compose.resources.stringResource
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun provideSelectionContainerCustomContextMenuForDesktop(
    onWebSearchClick: (url: String) -> Unit,
    buildSearchUrl: (encodedSelectedText: String) -> String,
    content: @Composable () -> Unit,
) {
    val baseContextMenu = LocalTextContextMenu.current

    CompositionLocalProvider(
        LocalTextContextMenu provides object : TextContextMenu {
            @Composable
            override fun Area(
                textManager: TextContextMenu.TextManager,
                state: ContextMenuState,
                content: @Composable () -> Unit,
            ) {
                val webSearchLabel = stringResource(DesignsystemRes.string.web_search)

                // Unless you explicitly add code to close the standard context menu after clicking, the context menu will not close.
                val closingManager = remember(textManager, state) {
                    ClosingTextManager(textManager, state)
                }

                ContextMenuDataProvider(
                    items = {
                        val selected = textManager.selectedText.toString().trim()
                        if (selected.isNotEmpty()) {
                            listOf(
                                ContextMenuItem(webSearchLabel) {
                                    state.status = ContextMenuState.Status.Closed

                                    val encoded = URLEncoder.encode(
                                        selected,
                                        StandardCharsets.UTF_8.toString(),
                                    )
                                    val url = buildSearchUrl(encoded)
                                    onWebSearchClick(url)
                                },
                            )
                        } else {
                            emptyList()
                        }
                    },
                ) {
                    baseContextMenu.Area(closingManager, state, content)
                }
            }
        },
        content = content,
    )
}

@OptIn(ExperimentalFoundationApi::class)
private class ClosingTextManager(
    private val delegate: TextContextMenu.TextManager,
    private val state: ContextMenuState,
) : TextContextMenu.TextManager {

    override val selectedText
        get() = delegate.selectedText

    override val cut: (() -> Unit)?
        get() = delegate.cut?.let { original ->
            {
                state.status = ContextMenuState.Status.Closed
                original()
            }
        }

    override val copy: (() -> Unit)?
        get() = delegate.copy?.let { original ->
            {
                state.status = ContextMenuState.Status.Closed
                original()
            }
        }

    override val paste: (() -> Unit)?
        get() = delegate.paste?.let { original ->
            {
                state.status = ContextMenuState.Status.Closed
                original()
            }
        }

    override val selectAll: (() -> Unit)?
        get() = delegate.selectAll?.let { original ->
            {
                state.status = ContextMenuState.Status.Closed
                original()
            }
        }

    override fun selectWordAtPositionIfNotAlreadySelected(offset: Offset) {
        delegate.selectWordAtPositionIfNotAlreadySelected(offset)
    }
}
