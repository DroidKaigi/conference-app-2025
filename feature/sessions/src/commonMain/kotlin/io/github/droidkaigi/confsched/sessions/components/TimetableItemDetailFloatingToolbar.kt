package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.add_to_bookmark
import io.github.droidkaigi.confsched.sessions.add_to_calendar
import io.github.droidkaigi.confsched.sessions.remove_from_bookmark
import io.github.droidkaigi.confsched.sessions.share_link
import io.github.droidkaigi.confsched.sessions.slide
import io.github.droidkaigi.confsched.sessions.video
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimetableItemDetailFloatingToolbar(
    expanded: Boolean,
    isBookmarked: Boolean,
    slideUrl: String?,
    videoUrl: String?,
    onBookmarkClick: (isBookmarked: Boolean) -> Unit,
    onAddCalendarClick: () -> Unit,
    onShareClick: () -> Unit,
    onViewSlideClick: (url: String) -> Unit,
    onWatchVideoClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalFloatingToolbar(
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.StandardFloatingActionButton(
                onClick = {
                    onBookmarkClick(!isBookmarked)
                }
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isBookmarked) {
                        stringResource(SessionsRes.string.remove_from_bookmark)
                    } else {
                        stringResource(SessionsRes.string.add_to_bookmark)
                    },
                )
            }
        },
        modifier = modifier,
        content = {
            IconButton(
                onClick = {
                    onAddCalendarClick()
                },
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = stringResource(SessionsRes.string.add_to_calendar))
            }
            IconButton(
                onClick = {
                    onShareClick()
                },
            ) {
                Icon(Icons.Default.Share, contentDescription = stringResource(SessionsRes.string.share_link))
            }
            slideUrl?.let { url ->
                IconButton(
                    onClick = {
                        onViewSlideClick(url)
                    },
                ) {
                    Icon(Icons.Outlined.Description, contentDescription = stringResource(SessionsRes.string.slide))
                }
            }
            videoUrl?.let { url ->
                IconButton(
                    onClick = {
                        onWatchVideoClick(url)
                    },
                ) {
                    Icon(Icons.Outlined.PlayCircle, contentDescription = stringResource(SessionsRes.string.video))
                }
            }
        },
    )
}

@Preview
@Composable
private fun TimetableItemDetailFloatingToolbarPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailFloatingToolbar(
                expanded = false,
                isBookmarked = false,
                onBookmarkClick = {},
                onAddCalendarClick = {},
                onShareClick = {},
                slideUrl = session.asset.slideUrl,
                videoUrl = session.asset.videoUrl,
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TimetableItemDetailFloatingToolbarExpandedPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailFloatingToolbar(
                expanded = true,
                isBookmarked = false,
                onBookmarkClick = {},
                onAddCalendarClick = {},
                onShareClick = {},
                slideUrl = session.asset.slideUrl,
                videoUrl = session.asset.videoUrl,
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}
