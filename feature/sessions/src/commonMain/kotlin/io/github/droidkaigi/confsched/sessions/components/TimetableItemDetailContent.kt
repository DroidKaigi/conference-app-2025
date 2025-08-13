package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.read_more
import io.github.droidkaigi.confsched.sessions.target_audience
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemDetailContent(
    timetableItem: TimetableItem,
    currentLang: Lang,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit,
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
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    onLinkClick: (url: String) -> Unit,
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var isOverFlow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
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
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = isExpand.not() && isOverFlow,
            enter = EnterTransition.None,
            exit = fadeOut(),
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalRoomTheme.current.dimColor
                ),
                border = null,
                onClick = { isExpand = true },
            ) {
                Text(
                    text = stringResource(SessionsRes.string.read_more),
                    style = MaterialTheme.typography.labelLarge,
                    color = LocalRoomTheme.current.primaryColor,
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
        Spacer(Modifier.height(8.dp))
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
            )
        }
    }
}
