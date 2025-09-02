package io.github.droidkaigi.confsched.droidkaigiui.session

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemTag
import io.github.droidkaigi.confsched.droidkaigiui.extension.icon
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.RoomType
import io.github.droidkaigi.confsched.model.core.toRoom
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TimetableItemLangTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = text,
        contentTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 6.dp),
        modifier = modifier,
    )
}

@Composable
internal fun TimetableItemRoomTag(
    icon: DrawableResource?,
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        icon = icon,
        tagText = text,
        contentTextColor = MaterialTheme.colorScheme.surface,
        contentBackgroundColor = color,
        borderColor = color,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 8.dp),
        modifier = modifier,
    )
}

@Composable
fun TimetableItemDateTag(
    dateText: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = dateText,
        contentTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 6.dp),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun TimetableItemLangTagPreview() {
    KaigiPreviewContainer {
        TimetableItemLangTag("JA")
    }
}

@Preview
@Composable
private fun TimetableItemRoomTagPreview() {
    KaigiPreviewContainer {
        Column {
            RoomType.entries.forEach { roomType ->
                TimetableItemRoomTag(
                    modifier = Modifier.padding(top = 22.dp),
                    icon = roomType.toRoom().icon,
                    text = roomType.toRoom().name.enTitle,
                    color = roomType.toRoom().roomTheme.primaryColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimetableItemDateTagPreview() {
    KaigiPreviewContainer {
        TimetableItemDateTag(dateText = "9/11")
    }
}
