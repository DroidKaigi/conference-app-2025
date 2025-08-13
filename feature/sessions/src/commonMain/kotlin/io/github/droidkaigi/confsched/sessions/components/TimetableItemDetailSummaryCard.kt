package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.Locale
import io.github.droidkaigi.confsched.model.core.getDefaultLocale
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.model.core.nameAndFloor
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.category_title
import io.github.droidkaigi.confsched.sessions.content_description_category
import io.github.droidkaigi.confsched.sessions.content_description_language
import io.github.droidkaigi.confsched.sessions.content_description_location
import io.github.droidkaigi.confsched.sessions.content_description_schedule
import io.github.droidkaigi.confsched.sessions.language_title
import io.github.droidkaigi.confsched.sessions.location_title
import io.github.droidkaigi.confsched.sessions.schedule_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemDetailSummaryCard(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LocalRoomTheme.current.dimColor)
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth(),
            imageVector = Icons.Outlined.Schedule,
            contentDescription = stringResource(SessionsRes.string.content_description_schedule),
            title = stringResource(SessionsRes.string.schedule_title),
            description = timetableItem.formattedDateTimeString,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth(),
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = stringResource(SessionsRes.string.content_description_location),
            title = stringResource(SessionsRes.string.location_title),
            description = timetableItem.room.nameAndFloor,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth(),
            imageVector = Icons.Outlined.Language,
            contentDescription = stringResource(SessionsRes.string.content_description_language),
            title = stringResource(SessionsRes.string.language_title),
            description = timetableItem.getSupportedLangString(
                getDefaultLocale() == Locale.JAPAN,
            ),
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth(),
            imageVector = Icons.Outlined.Category,
            contentDescription = stringResource(SessionsRes.string.content_description_category),
            title = stringResource(SessionsRes.string.category_title),
            description = timetableItem.category.title.currentLangTitle,
        )
    }
}

@Composable
private fun SummaryCardText(
    imageVector: ImageVector,
    contentDescription: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val iconInlineContentId = "icon"
    val spacer8dpInlineContentId = "spacer8dp"
    val spacer12dpInlineContentId = "spacer12dp"

    val annotatedString = createSummaryCardTextAnnotatedString(
        title = title,
        description = description,
        iconInlineContentId = iconInlineContentId,
        spacer8dpInlineContentId = spacer8dpInlineContentId,
        spacer12dpInlineContentId = spacer12dpInlineContentId,
    )

    val inlineContent = createInlineContentsMapForSummaryCardTexts(
        imageVector = imageVector,
        contentDescription = contentDescription,
        iconInlineContentId = iconInlineContentId,
        spacer8dpInlineContentId = spacer8dpInlineContentId,
        spacer12dpInlineContentId = spacer12dpInlineContentId,
    )

    // If multiple Texts are placed, they will not break lines properly when the font scale is large.
    // Therefore, AnnotatedString is used for implementation.
    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.Start),
    )
}

@Composable
private fun createSummaryCardTextAnnotatedString(
    title: String,
    description: String,
    iconInlineContentId: String,
    spacer8dpInlineContentId: String,
    spacer12dpInlineContentId: String,
): AnnotatedString {
    return buildAnnotatedString {
        appendInlineContent(id = iconInlineContentId, alternateText = "[icon]")
        appendInlineContent(id = spacer8dpInlineContentId, alternateText = " ")
        withStyle(
            style = SpanStyle(
                color = LocalRoomTheme.current.primaryColor,
                fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            ),
        ) {
            append(title)
        }
        appendInlineContent(
            id = spacer12dpInlineContentId,
            alternateText = " ",
        )
        withStyle(
            style = SpanStyle(
                color = LocalContentColor.current,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            ),
        ) {
            append(description)
        }
    }
}

@Composable
private fun createInlineContentsMapForSummaryCardTexts(
    imageVector: ImageVector,
    contentDescription: String,
    iconInlineContentId: String,
    spacer8dpInlineContentId: String,
    spacer12dpInlineContentId: String,
): Map<String, InlineTextContent> {
    return mapOf(
        iconInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = MaterialTheme.typography.titleSmall.fontSize,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = LocalRoomTheme.current.primaryColor,
                )
            },
        ),
        spacer8dpInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = 8.sp,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Spacer(modifier = Modifier.width(8.dp))
            },
        ),
        spacer12dpInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = 12.sp,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Spacer(modifier = Modifier.width(12.dp))
            },
        ),
    )
}

@Composable
@Preview
private fun TimetableItemDetailSummaryCardPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailSummaryCard(
                timetableItem = session,
            )
        }
    }
}
