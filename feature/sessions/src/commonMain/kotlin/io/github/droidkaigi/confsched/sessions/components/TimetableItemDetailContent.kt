package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.designsystem.component.provideSelectionContainerCustomContextMenuForDesktop
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.droidkaigiui.rememberBooleanSaveable
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.model.sessions.noAssetAvailableFake
import io.github.droidkaigi.confsched.model.sessions.onlySlideAssetAvailableFake
import io.github.droidkaigi.confsched.model.sessions.onlyVideoAssetAvailableFake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.archive
import io.github.droidkaigi.confsched.sessions.read_more
import io.github.droidkaigi.confsched.sessions.slide
import io.github.droidkaigi.confsched.sessions.target_audience
import io.github.droidkaigi.confsched.sessions.video
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TargetAudienceSectionTestTag = "TargetAudienceSectionTestTag"
const val DescriptionMoreButtonTestTag = "DescriptionMoreButtonTestTag"

const val TimetableItemDetailContentArchiveSectionTestTag = "TimetableItemDetailContentArchiveSectionTestTag"
const val TimetableItemDetailContentArchiveSectionSlideButtonTestTag = "TimetableItemDetailContentArchiveSectionSlideButtonTestTag"
const val TimetableItemDetailContentArchiveSectionVideoButtonTestTag = "TimetableItemDetailContentArchiveSectionVideoButtonTestTag"
const val TimetableItemDetailContentArchiveSectionBottomTestTag = "TimetableItemDetailContentArchiveSectionBottomTestTag"
const val TimetableItemDetailContentTargetAudienceSectionBottomTestTag = "TimetableItemDetailContentTargetAudienceSectionBottomTestTag"

@Composable
fun TimetableItemDetailContent(
    timetableItem: TimetableItem,
    currentLang: Lang,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit,
    onViewSlideClick: (url: String) -> Unit,
    onWatchVideoClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        DescriptionSection(
            description = when (timetableItem) {
                is TimetableItem.Session -> timetableItem.description.getByLang(currentLang)
                is TimetableItem.Special -> timetableItem.description.currentLangTitle
            },
            onLinkClick = onLinkClick,
        )
        TargetAudienceSection(targetAudience = timetableItem.targetAudience)

        if (timetableItem.asset.slideUrl != null || timetableItem.asset.videoUrl != null) {
            ArchiveSection(
                slideUrl = timetableItem.asset.slideUrl,
                videoUrl = timetableItem.asset.videoUrl,
                onViewSlideClick = onViewSlideClick,
                onWatchVideoClick = onWatchVideoClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DescriptionSection(
    description: String,
    onLinkClick: (url: String) -> Unit,
) {
    var isExpand by rememberBooleanSaveable(false)
    var isOverFlow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        provideSelectionContainerCustomContextMenuForDesktop(
            onWebSearchClick = { onLinkClick(it) },
        ) {
            SelectionContainer {
                ClickableLinkText(
                    content = description,
                    regex = "(https)(://[\\w/:%#$&?()~.=+\\-]+)".toRegex(),
                    onLinkClick = onLinkClick,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (isExpand) Int.MAX_VALUE else 7,
                    overflow = if (isExpand) TextOverflow.Clip else TextOverflow.Ellipsis,
                    onOverflow = {
                        isOverFlow = it
                    },
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = isExpand.not() && isOverFlow,
            enter = EnterTransition.None,
            exit = fadeOut(),
            modifier = Modifier.testTag(DescriptionMoreButtonTestTag),
        ) {
            ElevatedButton(
                modifier = Modifier
                    .semantics {
                        hideFromAccessibility()
                    }
                    .fillMaxWidth(),
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = LocalRoomTheme.current.dimColor,
                    contentColor = LocalRoomTheme.current.primaryColor,
                ),
                onClick = { isExpand = true },
            ) {
                Text(
                    text = stringResource(SessionsRes.string.read_more),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.semantics {
                        hideFromAccessibility()
                    },
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TargetAudienceSection(
    targetAudience: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .testTag(TargetAudienceSectionTestTag),
    ) {
        Text(
            text = stringResource(SessionsRes.string.target_audience),
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = targetAudience,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(8.dp).testTag(TimetableItemDetailContentTargetAudienceSectionBottomTestTag))
    }
}

@Composable
private fun ArchiveSection(
    slideUrl: String?,
    videoUrl: String?,
    onViewSlideClick: (url: String) -> Unit,
    onWatchVideoClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .testTag(TimetableItemDetailContentArchiveSectionTestTag),
    ) {
        Text(
            text = stringResource(SessionsRes.string.archive),
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            slideUrl?.let { url ->
                AssetButton(
                    modifier = Modifier
                        .testTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
                        .weight(1f),
                    iconVector = Icons.Outlined.Description,
                    text = stringResource(SessionsRes.string.slide),
                    onClick = { onViewSlideClick(url) },
                )
            }
            videoUrl?.let { url ->
                AssetButton(
                    modifier = Modifier
                        .testTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
                        .weight(1f),
                    iconVector = Icons.Outlined.PlayCircle,
                    text = stringResource(SessionsRes.string.video),
                    onClick = { onWatchVideoClick(url) },
                )
            }
        }
        Spacer(Modifier.height(8.dp).testTag(TimetableItemDetailContentArchiveSectionBottomTestTag))
    }
}

@Composable
private fun AssetButton(
    iconVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalRoomTheme.current.primaryColor,
        ),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.JAPANESE,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentWithEnglishPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.ENGLISH,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentWithMixedPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.MIXED,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentNoVideoPreview() {
    val session = TimetableItem.Session.onlySlideAssetAvailableFake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.JAPANESE,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentNoSlidePreview() {
    val session = TimetableItem.Session.onlyVideoAssetAvailableFake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.JAPANESE,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun TimetableItemDetailContentNoArchivePreview() {
    val session = TimetableItem.Session.noAssetAvailableFake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailContent(
                timetableItem = session,
                currentLang = Lang.JAPANESE,
                onLinkClick = {},
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}
