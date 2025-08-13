package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
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

@Composable
fun TimetableItemDetailScreen(
    uiState: TimetableItemDetailScreenUiState,
    onBackClick: () -> Unit,
    onBookmarkClick: (isBookmarked: Boolean) -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onLanguageSelect: (Lang) -> Unit,
    onLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideRoomTheme(uiState.timetableItem.room.roomTheme) {
        Scaffold(
            topBar = {
                TimetableItemDetailTopAppBar(
                    onBackClick = onBackClick,
                )
            },
            floatingActionButton = {
                TimetableItemDetailFloatingActionButtonMenu(
                    isBookmarked = uiState.isBookmarked,
                    slideUrl = uiState.timetableItem.asset.slideUrl,
                    videoUrl = uiState.timetableItem.asset.videoUrl,
                    onBookmarkClick = onBookmarkClick,
                    onAddCalendarClick = { onAddCalendarClick(uiState.timetableItem) },
                    onShareClick = { onShareClick(uiState.timetableItem) },
                    onViewSlideClick = onLinkClick,
                    onWatchVideoClick = onLinkClick
                )
            },
            modifier = modifier
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                            )
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
                        )
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
            onBookmarkClick = {},
            onAddCalendarClick = {},
            onShareClick = {},
            onLanguageSelect = {},
            onLinkClick = {},
        )
    }
}
