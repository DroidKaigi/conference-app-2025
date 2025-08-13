package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.droidkaigiui.extension.icon
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.RoomType
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.sessions.TimetableSpeaker
import io.github.droidkaigi.confsched.model.sessions.fake
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes


@Composable
fun TimetableGridItem(
    timetableItem: TimetableItem,
    onTimetableItemClick: (timetableItem: TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val height = remember(timetableItem) {
        TimetableGridItemDefaults.unitOfHeight * (timetableItem.minutes)
    }

    ProvideRoomTheme(timetableItem.room.roomTheme) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .clickable { onTimetableItemClick(timetableItem) }
                .background(LocalRoomTheme.current.containerColor)
                .width(TimetableGridItemDefaults.width)
                .height(height)
                .border(1.dp, LocalRoomTheme.current.primaryColor, RoundedCornerShape(16.dp))
                .padding(TimetableGridItemDefaults.contentPadding)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TimetableSchedule(
                    schedule = timetableItem.formattedTimeString,
                    icon = timetableItem.room.icon,
                )
                //  Trim spacing to prevent the title from overflowing if minutes is < 30min
                if (timetableItem.minutes > 30) {
                    Spacer(modifier = Modifier.height(TimetableGridItemDefaults.scheduleToTitleSpace))
                }
                TimetableTitle(timetableItem.title.currentLangTitle)
            }
            timetableItem.speakers.firstOrNull()?.let { speaker ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TimetableSpeaker(
                        speaker = speaker,
                        modifier = Modifier.weight(1f),
                    )
                    if (timetableItem.message != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(TimetableGridItemDefaults.errorSize),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimetableSchedule(
    schedule: String,
    icon: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        if (icon != null) {
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = LocalRoomTheme.current.primaryColor,
                modifier = Modifier.height(TimetableGridItemDefaults.scheduleHeight),
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = schedule,
            style = MaterialTheme.typography.labelSmall,
            color = LocalRoomTheme.current.primaryColor,
        )
    }
}

@Composable
private fun TimetableSpeaker(
    speaker: TimetableSpeaker,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(speaker.iconUrl)
    Row(modifier) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
        )
        Text(
            text = speaker.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
        )
    }
}


@Composable
private fun TimetableTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.labelLarge.copy(
        color = LocalRoomTheme.current.primaryColor,
    )
    Text(
        text = title,
        style = textStyle,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        autoSize = TextAutoSize.StepBased(
            minFontSize = TimetableGridItemDefaults.minTitleFontSize,
            maxFontSize = TimetableGridItemDefaults.maxTitleFontSize,
        ),
        modifier = modifier,
    )
}

private object TimetableGridItemDefaults {
    val width = 192.dp
    val unitOfHeight = 4.dp // 1 minute = 4dp
    val contentPadding = 12.dp
    val scheduleToTitleSpace = 6.dp
    val scheduleHeight = 16.dp
    val errorSize = 16.dp
    val minTitleFontSize = 10.sp
    val maxTitleFontSize = 14.sp
}

@Preview
@Composable
private fun TimetableGridItemPreview() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake().copy(
                message = null,
            ),
            onTimetableItemClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableGridItemPreview_80min() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake(80.minutes).copy(
                message = null,
            ),
            onTimetableItemClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableGridItemPreview_WelcomeTalk() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake(15.minutes).copy(
                message = null,
                title = MultiLangText(
                    jaTitle = "Welcome Talk",
                    enTitle = "Welcome Talk",
                ),
                speakers = persistentListOf(),
                room = Room(
                    id = 1,
                    name = MultiLangText("Jellyfish", "Jellyfish"),
                    type = RoomType.RoomJ,
                    sort = 1,
                ),
            ),
            onTimetableItemClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableGridItemPreview_LongTitme() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake().copy(
                message = null,
                title = MultiLangText(
                    jaTitle = "Material3 マイグレーションMaterial3 マイグレーションMaterial3 マイグレーションMaterial3 マイグレーション",
                    enTitle = "Material3 Migration Material3 Migration Material3 Migration Material3 Migration",
                ),
            ),
            onTimetableItemClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableGridItemPreview_WithError() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake(),
            onTimetableItemClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableGridItemPreview_NoSpeaker() {
    KaigiPreviewContainer {
        TimetableGridItem(
            timetableItem = TimetableItem.Session.fake().copy(
                speakers = persistentListOf(),
            ),
            onTimetableItemClick = {},
        )
    }
}
