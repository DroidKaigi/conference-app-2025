package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.enableMouseDragScroll
import io.github.droidkaigi.confsched.droidkaigiui.extension.plus
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailAnnounceMessage
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContent
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailFloatingActionButtonMenu
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailHeadline
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailSummaryCard
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailTopAppBar
import org.jetbrains.compose.ui.tooling.preview.Preview

// TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
// const val TimetableItemDetailBookmarkIconTestTag = "TimetableItemDetailBookmarkIconTestTag"
const val TimetableItemDetailScreenLazyColumnTestTag = "TimetableItemDetailScreenLazyColumnTestTag"

@Composable
fun TimetableItemDetailScreen(
    uiState: TimetableItemDetailScreenUiState,
    onBackClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onLanguageSelect: (Lang) -> Unit,
    onLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideRoomTheme(uiState.timetableItem.room.roomTheme) {
        val listState = rememberLazyListState()
        var fabHeight by remember { mutableStateOf(0.dp) }
        Scaffold(
            topBar = {
                TimetableItemDetailTopAppBar(
                    onBackClick = onBackClick,
                )
            },
            floatingActionButton = {
                val density = LocalDensity.current
                TimetableItemDetailFloatingActionButtonMenu(
                    isBookmarked = uiState.isBookmarked,
                    slideUrl = uiState.timetableItem.asset.slideUrl,
                    videoUrl = uiState.timetableItem.asset.videoUrl,
                    onBookmarkToggle = onBookmarkToggle,
                    onAddCalendarClick = { onAddCalendarClick(uiState.timetableItem) },
                    onShareClick = { onShareClick(uiState.timetableItem) },
                    onViewSlideClick = onLinkClick,
                    onWatchVideoClick = onLinkClick,
                    modifier = Modifier.onGloballyPositioned {
                        with(density) {
                            fabHeight = it.size.height.toDp()
                        }
                    }.padding(bottom = WindowInsets.safeGestures.asPaddingValues().calculateBottomPadding()),
                )
            },
            contentWindowInsets = WindowInsets(),
            modifier = modifier,
        ) { contentPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .enableMouseDragScroll(listState)
                    .testTag(TimetableItemDetailScreenLazyColumnTestTag),
                contentPadding = contentPadding + PaddingValues(bottom = WindowInsets.safeGestures.asPaddingValues().calculateBottomPadding() + fabHeight),
            ) {
                item {
                    TimetableItemDetailHeadline(
                        currentLang = uiState.currentLang,
                        timetableItem = uiState.timetableItem,
                        isLangSelectable = uiState.isLangSelectable,
                        onLanguageSelect = onLanguageSelect,
                    )
                }

                when (uiState.timetableItem) {
                    is TimetableItem.Session -> uiState.timetableItem.message
                    is TimetableItem.Special -> uiState.timetableItem.message
                }?.let {
                    item {
                        TimetableItemDetailAnnounceMessage(
                            message = it.currentLangTitle,
                            modifier = Modifier.padding(
                                start = 8.dp,
                                top = 24.dp,
                                end = 8.dp,
                                bottom = 4.dp,
                            ),
                        )
                    }
                }

                item {
                    TimetableItemDetailSummaryCard(
                        timetableItem = uiState.timetableItem,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 16.dp,
                            bottom = 8.dp,
                        ),
                    )
                }

                item {
                    TimetableItemDetailContent(
                        timetableItem = uiState.timetableItem,
                        currentLang = uiState.currentLang,
                        onLinkClick = onLinkClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimetableItemDetailScreenPreview() {
    KaigiPreviewContainer {
        TimetableItemDetailScreen(
            uiState = TimetableItemDetailScreenUiState(
                timetableItem = TimetableItem.Session.fake(),
                isBookmarked = false,
                currentLang = Lang.JAPANESE,
                isLangSelectable = true,
            ),
            onBackClick = {},
            onBookmarkToggle = {},
            onAddCalendarClick = {},
            onShareClick = {},
            onLanguageSelect = {},
            onLinkClick = {},
        )
    }
}
